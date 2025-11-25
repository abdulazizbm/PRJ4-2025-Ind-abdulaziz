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
  RestBindings,
  Request,
} from '@loopback/rest';
import {Transaction} from '../models';
import {TransactionRepository} from '../repositories';
import {inject} from '@loopback/core';
import { SecurityBindings, UserProfile } from '@loopback/security';
import { authenticate } from '@loopback/authentication';
import { StripeService } from '../service/StripeService';

export class TransactionController {
  constructor(
    @repository(TransactionRepository)
    public transactionRepository : TransactionRepository,

    @inject('services.StripeService')
    private stripeService: StripeService

  ) {}
  
  @post('/transactions')
  @response(200, {
    description: 'Transaction model instance',
    content: {'application/json': {schema: getModelSchemaRef(Transaction)}},
  })
  async create(
    @inject(SecurityBindings.USER) user: UserProfile,
    @requestBody({
      content: { 'application/json': {
        schema: getModelSchemaRef(Transaction, {
          title: 'NewTransaction',
          exclude: ['_id', 'buyer_id'],   // exclude the FK field too
        }),
      }},
    })
    transactionData: Omit<Transaction, '_id' | 'buyer_id'>,  // rename & omit snake_case FK
  ): Promise<Transaction> {
    return this.transactionRepository.create({
      ...transactionData,
      buyer_id: user.id,             // user.id is already a string
    });
  }

  @get('/transactions/count')
  @response(200, {
    description: 'Transaction model count',
    content: {'application/json': {schema: CountSchema}},
  })
  async count(
    @param.where(Transaction) where?: Where<Transaction>,
  ): Promise<Count> {
    return this.transactionRepository.count(where);
  }

  @get('/transactions')
  @response(200, {
    description: 'Array of Transaction model instances',
    content: {
      'application/json': {
        schema: {
          type: 'array',
          items: getModelSchemaRef(Transaction, {includeRelations: true}),
        },
      },
    },
  })
  async find(
    @param.filter(Transaction) filter?: Filter<Transaction>,
  ): Promise<Transaction[]> {
    return this.transactionRepository.find(filter);
  }

  @patch('/transactions')
  @response(200, {
    description: 'Transaction PATCH success count',
    content: {'application/json': {schema: CountSchema}},
  })
  async updateAll(
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Transaction, {partial: true}),
        },
      },
    })
    transaction: Transaction,
    @param.where(Transaction) where?: Where<Transaction>,
  ): Promise<Count> {
    return this.transactionRepository.updateAll(transaction, where);
  }

  @get('/transactions/{id}')
  @response(200, {
    description: 'Transaction model instance',
    content: {
      'application/json': {
        schema: getModelSchemaRef(Transaction, {
          includeRelations: true
        }),
      },
    },
  })
  async findById(
    @param.path.string('id') id: string,
    @param.filter(Transaction, {exclude: 'where'}) filter?: FilterExcludingWhere<Transaction>
  ): Promise<Transaction> {
    return this.transactionRepository.findById(id, filter);
  }

  @patch('/transactions/{id}')
  @response(204, {
    description: 'Transaction PATCH success',
  })
  async updateById(
    @param.path.string('id') id: string,
    @requestBody({
      content: {
        'application/json': {
          schema: getModelSchemaRef(Transaction, {exclude: ['_id', 'buyer_id'], partial: true}),
        },
      },
    })
    transaction: Omit<Transaction, 'transactionId' | 'buyerId'>,
  ): Promise<void> {
    await this.transactionRepository.updateById(id, transaction);
  }

  @put('/transactions/{id}')
  @response(204, {
    description: 'Transaction PUT success',
  })
  async replaceById(
    @param.path.string('id') id: string,
    @requestBody() transaction: Transaction,
  ): Promise<void> {
    await this.transactionRepository.replaceById(id, transaction);
  }

  @del('/transactions/{id}')
  @response(204, {
    description: 'Transaction DELETE success',
  })
  async deleteById(@param.path.string('id') id: string): Promise<void> {
    await this.transactionRepository.deleteById(id);
  }

  @post('/stripe/create-checkout')
  @authenticate('jwt')
  @response(200, {
    description: 'Create Stripe Checkout Session',
  })
  async createStripeCheckout(
  @inject(SecurityBindings.USER) user: UserProfile,
  @requestBody({
    content: { 'application/json': {
      schema: {
        type: 'object',
        required: ['amount','currency','seller_id','product_id'],
        properties: {
          amount:     {type: 'number'},
          currency:   {type: 'string'},
          seller_id:  {type: 'string'},
          product_id: {type: 'string'},
        },
      },
    }},
  })
  body: {amount: number; currency: string; seller_id: string; product_id: string},
  ) {
    return this.stripeService.createCheckoutSession(
      body.amount,
      body.currency,
      body.seller_id,
      body.product_id,
      user.id,
    );
  }
}
