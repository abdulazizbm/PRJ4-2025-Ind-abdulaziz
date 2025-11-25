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
import {product_Img} from '../models';
import {ProductImgRepository} from '../repositories';

export class ProductImgController {
  constructor(
    @repository(ProductImgRepository)
    public productImgRepository : ProductImgRepository,
  ) {}

  @post('/product-imgs')
  @response(200, {
    description: 'ProductImg model instance',
    content: {'application/json': {schema: getModelSchemaRef(product_Img)}},
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(product_Img, {
            title: 'NewProductImg',
            exclude: ['_id'],
          }),
        },
      },
    })
    product_Img: Omit<product_Img, '_id'>,
  ): Promise<product_Img> {
    return this.productImgRepository.create(product_Img);
  }

  @get('/product-imgs/count')
  @response(200, {
    description: 'ProductImg model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(product_Img) where?: Where<product_Img>,
  ): Promise<Count> {
    return this.productImgRepository.count(where);
  }

  @get('/product-imgs')
  @response(200, {
    description: 'Array of ProductImg model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(product_Img, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(product_Img) filter?: Filter<product_Img>,
  ): Promise<product_Img[]> {
    return this.productImgRepository.find(filter);
  }

  @patch('/product-imgs')
  @response(200, {
    description: 'ProductImg PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(product_Img, {partial: true}),
        },
      },
    })
    productImg: product_Img,
    @param.where(product_Img) where?: Where<product_Img>,
  ): Promise<Count> {
    return this.productImgRepository.updateAll(productImg, where);
  }

  @get('/product-imgs/{id}')
  @response(200, {
    description: 'ProductImg model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(product_Img, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(product_Img, {exclude: 'where'}) filter?: FilterExcludingWhere<product_Img>
  ): Promise<product_Img> {
    return this.productImgRepository.findById(id, filter);
  }

  @patch('/product-imgs/{id}')
  @response(204, {
    description: 'ProductImg PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(product_Img, {partial: true}),
        },
      },
    })
    productImg: product_Img,
  ): Promise<void> {
    await this.productImgRepository.updateById(id, productImg);
  }

  @put('/product-imgs/{id}')
  @response(204, {
    description: 'ProductImg PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() productImg: product_Img,
  ): Promise<void> {
    await this.productImgRepository.replaceById(id, productImg);
  }

  @del('/product-imgs/{id}')
  @response(204, {
    description: 'ProductImg DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.productImgRepository.deleteById(id);
  }

@get('/product-imgs/by-product/{productId}')
@response(200, {
  description: 'Find ProductImg by productId',
  content: {'application/json': {schema: getModelSchemaRef(product_Img)}},
})
async findByProductId(
  @param.path.string('productId') productId: string,
): Promise<product_Img | null> {
  const result = await this.productImgRepository.findOne({
    where: {product_id:productId},
  });
  return result;
}

}
