import {
  Count,
  CountSchema,
  Filter,
  FilterExcludingWhere,
  repository,
  Where,
} from '@loopback/repository';
import {
  post,
  param,
  get,
  getModelSchemaRef,
  patch,
  put,
  del,
  requestBody,
  response,
} from '@loopback/rest';
import {UserImg} from '../models';
import {UserImgRepository} from '../repositories';

export class UserImgController {
  constructor(
    @repository(UserImgRepository)
    public userImgRepository : UserImgRepository,
  ) {}

  @post('/user-imgs')
  @response(200, {
    description: 'UserImg model instance',
    content: {'application/json': {schema: getModelSchemaRef(UserImg)}},
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(UserImg, {
            title: 'NewUserImg',
            exclude: ['_id'],
          }),
        },
      },
    })
    userImg: Omit<UserImg, 'userImgId'>,
  ): Promise<UserImg> {
    return this.userImgRepository.create(userImg);
  }

  @get('/user-imgs/count')
  @response(200, {
    description: 'UserImg model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(UserImg) where?: Where<UserImg>,
  ): Promise<Count> {
    return this.userImgRepository.count(where);
  }

  @get('/user-imgs')
  @response(200, {
    description: 'Array of UserImg model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(UserImg, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(UserImg) filter?: Filter<UserImg>,
  ): Promise<UserImg[]> {
    return this.userImgRepository.find(filter);
  }

  @patch('/user-imgs')
  @response(200, {
    description: 'UserImg PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(UserImg, {partial: true}),
        },
      },
    })
    userImg: UserImg,
    @param.where(UserImg) where?: Where<UserImg>,
  ): Promise<Count> {
    return this.userImgRepository.updateAll(userImg, where);
  }

  @get('/user-imgs/{id}')
  @response(200, {
    description: 'UserImg model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(UserImg, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(UserImg, {exclude: 'where'}) filter?: FilterExcludingWhere<UserImg>
  ): Promise<UserImg> {
    return this.userImgRepository.findById(id, filter);
  }

  @patch('/user-imgs/{id}')
  @response(204, {
    description: 'UserImg PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(UserImg, {partial: true}),
        },
      },
    })
    userImg: UserImg,
  ): Promise<void> {
    await this.userImgRepository.updateById(id, userImg);
  }

  @put('/user-imgs/{id}')
  @response(204, {
    description: 'UserImg PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() userImg: UserImg,
  ): Promise<void> {
    await this.userImgRepository.replaceById(id, userImg);
  }

  @del('/user-imgs/{id}')
  @response(204, {
    description: 'UserImg DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.userImgRepository.deleteById(id);
  }
}
