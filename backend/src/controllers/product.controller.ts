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
  HttpErrors,
} from '@loopback/rest';
import {Product} from '../models';
import {ProductRepository} from '../repositories';
import { authenticate } from '@loopback/authentication';
import { SecurityBindings, UserProfile } from '@loopback/security';
import { inject } from '@loopback/core';
import {ObjectId} from 'mongodb';


export class ProductController {
  constructor(
    @repository(ProductRepository)
    public productRepository : ProductRepository,
  ) {}

  @authenticate('jwt')
  @post('/products')
  @response(200, {
    description: 'Product model instance',
    content: {'application/json': {schema: getModelSchemaRef(Product)}},
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Product, {
            title: 'NewProduct',
            exclude: ['_id', 'seller_id'], 
          }),
        },
      },
    })
    product: Omit<Product, '_id' | 'seller_id'>,
    @inject(SecurityBindings.USER) currentUser: UserProfile,
  ): Promise<Product> {
    if (!currentUser.id) {
    throw new HttpErrors.Unauthorized('User ID not found in token');
    }
      const productToCreate = {
    ...product,
    seller_id: currentUser.id, 
    created_at: new Date().toISOString(),
  };
    console.log('Product to create:', productToCreate); 
    return this.productRepository.create(productToCreate);
  }

  @get('/products/count')
  @response(200, {
    description: 'Product model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(Product) where?: Where<Product>,
  ): Promise<Count> {
    return this.productRepository.count(where);
  }

  @get('/products')
  @response(200, {
    description: 'Array of Product model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(Product, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(Product) filter?: Filter<Product>,
  ): Promise<Product[]> {
    return this.productRepository.find(filter);
  }

  @get('/product/search')
  @response(200, {
    description: 'Retrieval of searched products',
    content:{
      'application/json': {
        schema: {type: 'array'
        },
      },
    },
  })
  async findSearchedProducts(
    @param.query.string('search') search: string,
  
  ): Promise<Product[]> {
    return this.productRepository.find({
      where: {title: {ilike: `${search}%`}},
    });
  }

  @patch('/products')
  @response(200, {
    description: 'Product PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Product, {partial: true}),
        },
      },
    })
    product: Product,
    @param.where(Product) where?: Where<Product>,
  ): Promise<Count> {
    return this.productRepository.updateAll(product, where);
  }

  @get('/products/{id}')
  @response(200, {
    description: 'Product model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(Product, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.number('id') id: string,
    @param.filter(Product, {exclude: 'where'}) filter?: FilterExcludingWhere<Product>
  ): Promise<Product> {
    return this.productRepository.findById(id, filter);
  }

  @patch('/products/{id}')
  @response(204, {
    description: 'Product PATCH success',
  })
  async updateById(
    @param.path.number('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Product, {partial: true}),
        },
      },
    })
    product: Product,
  ): Promise<void> {
    await this.productRepository.updateById(id, product);
  }

  @put('/products/{id}')
  @response(204, {
    description: 'Product PUT success',
  })
  async replaceById(
    @param.path.number('id') id: string,
    @requestBody() product: Product,
  ): Promise<void> {
    await this.productRepository.replaceById(id, product);
  }

  @del('/products/{id}')
  @response(204, {
    description: 'Product DELETE success',
  })
  async deleteById(@param.path.number('id') id: string): Promise<void> {
    await this.productRepository.deleteById(id);
  }

  @authenticate('jwt')
@get('/debug/user')
async debugUser(
  @inject(SecurityBindings.USER) currentUser: UserProfile
) {
  return {
    userId: currentUser.id,
    message: "This is the user extracted from the JWT token"
  };
}
}
