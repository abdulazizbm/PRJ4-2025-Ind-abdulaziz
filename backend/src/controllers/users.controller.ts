import {inject} from '@loopback/core';
import {
  Count,
  CountSchema,
  Filter,
  FilterExcludingWhere,
  repository,
  Where,
} from '@loopback/repository';
import {
  del,
  get,
  getModelSchemaRef,
  HttpErrors,
  param,
  patch,
  post,
  put,
  requestBody,
  response,
} from '@loopback/rest';
import {compare, genSalt, hash} from 'bcryptjs';
import {Users} from '../models';
import {UsersRepository} from '../repositories';
import {Service as JwtService} from '../service/jwtService';
import { SecurityBindings, UserProfile } from '@loopback/security';
import {authenticate} from '@loopback/authentication';
import { log } from 'console';


export class UsersController {
  constructor(
    @repository(UsersRepository)
    public usersRepository: UsersRepository,
    @inject('services.jwtService')
    public jwtService: JwtService,
  ) { }

  @post('/users')
  @response(200, {
    description: 'Users model instance',
    content: {'application/json': {schema: getModelSchemaRef(Users)}},
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Users, {
            title: 'NewUsers',
            exclude: ['_id'],
          }),
        },
      },
    })
    usersData: Omit<Users, 'user_id'>,
  ): Promise<Omit<Users, 'password'>> {
    const salt = await genSalt(10);
    const hashed = await hash(usersData.password, salt);
    const saved = await this.usersRepository.create({
      ...usersData,
      password: hashed,
    });
    const {password, ...userWithoutPassword} = saved as any;
    return userWithoutPassword;
  }

  @get('/users/count')
  @response(200, {
    description: 'Users model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(Users) where?: Where<Users>,
  ): Promise<Count> {
    return this.usersRepository.count(where);
  }

  @get('/users')
  @response(200, {
    description: 'Array of Users model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(Users, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(Users) filter?: Filter<Users>,
  ): Promise<Users[]> {
    return this.usersRepository.find(filter);
  }

  @authenticate('jwt')
  @get('/users/me')
  @response(200, {
  description: 'Return the currently authenticated user',
  content: {'application/json': {schema: getModelSchemaRef(Users)}},
  })
  async getCurrentUser(
  @inject(SecurityBindings.USER) user: UserProfile
  ): Promise<Users> {
  const userId = user.id; // Comes from JWT payload
  const currentUser = await this.usersRepository.findById(userId);
  return currentUser;
  }

  @patch('/users')
  @response(200, {
    description: 'Users PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Users, {partial: true}),
        },
      },
    })
    users: Users,
    @param.where(Users) where?: Where<Users>,
  ): Promise<Count> {
    return this.usersRepository.updateAll(users, where);
  }

  @get('/users/{id}')
  @response(200, {
    description: 'Users model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(Users, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(Users, {exclude: 'where'}) filter?: FilterExcludingWhere<Users>
  ): Promise<Users> {
    return this.usersRepository.findById(id, filter);
  }

  @patch('/users/{id}')
  @response(204, {
    description: 'Users PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Users, {partial: true}),
        },
      },
    })
    users: Users,
  ): Promise<void> {
    if (users.password) {
    users.password = await hash(users.password, await genSalt(10));
  }
    await this.usersRepository.updateById(id, users);
  }

  @put('/users/{id}')
  @response(204, {
    description: 'Users PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() users: Users,
  ): Promise<void> {
    await this.usersRepository.replaceById(id, users);
  }

  @del('/users/{id}')
  @response(204, {
    description: 'Users DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.usersRepository.deleteById(id);
  }

  @post('/users/login')
  @response(200, {
    description: 'Member login',
    content: {
      'application/json': {
        schema: {
          type: 'object',
          required: ['email', 'password'],
          properties: {
            token: {type: 'string'},
            refreshToken: {type: 'string'},
          },
        },
      },
    },
  })
  async login(
    @requestBody({
      content: {
        'application/json': {
          schema: {
            type: 'object',
            required: ['email', 'password'],
            properties: {
              email: {type: 'string'},
              password: {type: 'string'},
            },
          },
        },
      },
    })
    credentials: {email: string; password: string},
  ): Promise<{token: string; refreshToken: string}> {
    const member = await this.usersRepository.findOne({
      where: {email: credentials.email},
    });
    if (!member) {
      throw new HttpErrors.Unauthorized('Invalid email or password');
    }

    const isMatch = await compare(credentials.password, member.password);
    if (!isMatch) {
      throw new HttpErrors.Unauthorized('Invalid email or password');
    }

    const token = await this.jwtService.generateToken(member);
    const refreshToken = await this.jwtService.generateRefreshToken(member);

    return {token, refreshToken};
  }

  @post('/users/refresh-token')
  @response(200, {
    description: 'Refresh access token',
    content: {
      'application/json': {
        schema: {
          type: 'object',
          required: ['refreshToken'],
          properties: {
            token: {type: 'string'},
            refreshToken: {type: 'string'},
          },
        },
      },
    },
  })
  async refreshToken(
    @requestBody({
      content: {
        'application/json': {
          schema: {
            type: 'object',
            required: ['refreshToken'],
            properties: {refreshToken: {type: 'string'}},
          },
        },
      },
    })
    body: {refreshToken: string},
  ): Promise<{token: string; refreshToken: string}> {
    const isBlacklisted = await this.jwtService.isRefreshTokenBlacklisted(body.refreshToken);
    if (isBlacklisted) {
    throw new HttpErrors.Unauthorized('Refresh token has been invalidated');
    }
    // verify the refresh token
    const decoded = await this.jwtService.verifyRefreshToken(body.refreshToken);
    // build a dummy Users for token generation
    const dummyUser: Users = Object.assign(new Users(), {
      _id: decoded.id,
      email: decoded.email,
    });
    const token = await this.jwtService.generateToken(dummyUser);
    const refreshToken = await this.jwtService.generateRefreshToken(dummyUser);
    return {token, refreshToken};
  }

  @post('/users/logout')
  @response(204, {
    description: 'Logout user',
  })
  async logout(
    @requestBody({
      content: {
        'application/json': {
          schema: {
            type: 'object',
            required: ['refreshToken'],
            properties: {
              refreshToken: {type: 'string'},
            },
          },
        },
      },
    })
    body: {refreshToken: string},
  ): Promise<void> {
    await this.jwtService.invalidateRefreshToken(body.refreshToken);
  }
}
