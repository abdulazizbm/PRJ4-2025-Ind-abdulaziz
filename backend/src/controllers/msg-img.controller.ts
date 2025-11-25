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
import {MessageImg} from '../models';
import {MessageImgRepository} from '../repositories';

export class MsgImgController {
  constructor(
    @repository(MessageImgRepository)
    public messageImgRepository : MessageImgRepository,
  ) {}

  @post('/message-imgs')
  @response(200, {
    description: 'MessageImg model instance',
    content: {'application/json': {schema: getModelSchemaRef(MessageImg)}},
  })
  async create(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(MessageImg, {
            title: 'NewMessageImg',
            exclude: ['_id'],
          }),
        },
      },
    })
    messageImg: Omit<MessageImg, 'msgImgId'>,
  ): Promise<MessageImg> {
    return this.messageImgRepository.create(messageImg);
  }

  @get('/message-imgs/count')
  @response(200, {
    description: 'MessageImg model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(MessageImg) where?: Where<MessageImg>,
  ): Promise<Count> {
    return this.messageImgRepository.count(where);
  }

  @get('/message-imgs')
  @response(200, {
    description: 'Array of MessageImg model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(MessageImg, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(MessageImg) filter?: Filter<MessageImg>,
  ): Promise<MessageImg[]> {
    return this.messageImgRepository.find(filter);
  }

  @patch('/message-imgs')
  @response(200, {
    description: 'MessageImg PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(MessageImg, {partial: true}),
        },
      },
    })
    messageImg: MessageImg,
    @param.where(MessageImg) where?: Where<MessageImg>,
  ): Promise<Count> {
    return this.messageImgRepository.updateAll(messageImg, where);
  }

  @get('/message-imgs/{id}')
  @response(200, {
    description: 'MessageImg model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(MessageImg, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(MessageImg, {exclude: 'where'}) filter?: FilterExcludingWhere<MessageImg>
  ): Promise<MessageImg> {
    return this.messageImgRepository.findById(id, filter);
  }

  @patch('/message-imgs/{id}')
  @response(204, {
    description: 'MessageImg PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(MessageImg, {partial: true}),
        },
      },
    })
    messageImg: MessageImg,
  ): Promise<void> {
    await this.messageImgRepository.updateById(id, messageImg);
  }

  @put('/message-imgs/{id}')
  @response(204, {
    description: 'MessageImg PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() messageImg: MessageImg,
  ): Promise<void> {
    await this.messageImgRepository.replaceById(id, messageImg);
  }

  @del('/message-imgs/{id}')
  @response(204, {
    description: 'MessageImg DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.messageImgRepository.deleteById(id);
  }

  @get('/message-imgs/by-message/{messageId}')
@response(200, {
  description: 'Image for a specific message',
  content: {
    'application/json': {
      schema: getModelSchemaRef(MessageImg),
    },
  },
})
async findByMessageId(
  @param.path.string('messageId') messageId: string
): Promise<MessageImg | null> {
  const images = await this.messageImgRepository.find({
    where: {_id: messageId},
    limit: 1,
  });

  return images[0] ?? null;
}

}
