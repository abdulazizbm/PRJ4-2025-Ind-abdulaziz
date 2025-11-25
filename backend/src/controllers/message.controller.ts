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
import {Message} from '../models';
import {MessageRepository} from '../repositories';

export class MessageController {
  constructor(
    @repository(MessageRepository)
    public messageRepository : MessageRepository,
  ) {}

 @post('/messages')
@response(200, {
  description: 'Message model instance',
  content: {'application/json': {schema: getModelSchemaRef(Message)}},
})
async create(
  @requestBody({
    content: {
      'application/json': {
        schema: getModelSchemaRef(Message, {
          title: 'NewMessage',
          exclude: ['_id'],
        }),
      },
    },
  })
  message: Omit<Message, 'message_id'>,
): Promise<Message> {
  console.log('Creating message:', message); 
  const created = await this.messageRepository.create(message);
  console.log('Saved message:', created); 
  return created;
}


  @get('/messages/count')
  @response(200, {
    description: 'Message model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(Message) where?: Where<Message>,
  ): Promise<Count> {
    return this.messageRepository.count(where);
  }

  @get('/messages')
  @response(200, {
    description: 'Array of Message model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(Message, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(Message) filter?: Filter<Message>,
  ): Promise<Message[]> {
    return this.messageRepository.find(filter);
  }

  @patch('/messages')
  @response(200, {
    description: 'Message PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Message, {partial: true}),
        },
      },
    })
    message: Message,
    @param.where(Message) where?: Where<Message>,
  ): Promise<Count> {
    return this.messageRepository.updateAll(message, where);
  }

  @get('/messages/{id}')
  @response(200, {
    description: 'Message model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(Message, {includeRelations: true}),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(Message, {exclude: 'where'}) filter?: FilterExcludingWhere<Message>
  ): Promise<Message> {
    return this.messageRepository.findById(id, filter);
  }

  @patch('/messages/{id}')
  @response(204, {
    description: 'Message PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Message, {partial: true}),
        },
      },
    })
    message: Message,
  ): Promise<void> {
    await this.messageRepository.updateById(id, message);
  }

  @put('/messages/{id}')
  @response(204, {
    description: 'Message PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() message: Message,
  ): Promise<void> {
    await this.messageRepository.replaceById(id, message);
  }

  @del('/messages/{id}')
  @response(204, {
    description: 'Message DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.messageRepository.deleteById(id);
  }

@get('/messages/by-users/{user1Id}/{user2Id}/{productId}')
@response(200, {
  description: 'Get all messages between two users for a product',
})
async findChatBetweenUsers(
  @param.path.string('user1Id') user1Id: string,
  @param.path.string('user2Id') user2Id: string,
  @param.path.string('productId') productId: string
): Promise<Message[]> { 
  console.log(`Fetching messages between ${user1Id} and ${user2Id} for product ${productId}`);
  const messages = await this.messageRepository.find({
  where: {
    or: [
      { sender_id: user1Id, receiver_id: user2Id, product_id: productId },
      { sender_id: user2Id, receiver_id: user1Id, product_id: productId },
    ],
  },
  order: ['sent_at ASC'],
});
console.log('Found messages:', messages);
return messages;

}


@get('/messages/overview/{userId}')
@response(200, {
  description: 'Latest messages by conversation',
})
async getUserChats(
  @param.path.string('userId') userId: string
): Promise<Message[]> {
  const all = await this.messageRepository.find({
    where: {
      or: [
        {sender_id: userId},
        {receiver_id: userId},
      ],
    },
    order: ['sent_at DESC'],
  });

  const seen = new Set<string>();
  const latestPerThread: Message[] = [];

  for (const msg of all) {
    const key = [msg.sender_id, msg.receiver_id, msg.product_id].sort().join("-");
    if (!seen.has(key)) {
      seen.add(key);
      latestPerThread.push(msg);
    }
  }

  return latestPerThread;
}




}
