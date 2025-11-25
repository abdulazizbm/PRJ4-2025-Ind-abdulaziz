import {Entity, model, property} from '@loopback/repository';

@model()
export class Transaction extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
  })
  _id?: string;

  @property({
    type: 'string',
    required: true,
  })
  buyer_id: string;

  @property({
    type: 'string',
    required: true,
  })
  product_id: string;

  @property({
    type: 'string', 
    required: true, 
  })
  seller_id: string;

  @property({
    type: 'date',
    required: true,
  })
  transaction_date: string;


  constructor(data?: Partial<Transaction>) {
    super(data);
  }
}

export interface TransactionRelations {
  // describe navigational properties here
}

export type TransactionWithRelations = Transaction & TransactionRelations;
