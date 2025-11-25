import {Entity, model, property} from '@loopback/repository';

@model()
export class Message extends Entity {
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
  content: string;

  @property({
    type: 'date',
  })
  sent_at?: string;

  @property({
    type: 'string',
    required: true,
  })
  sender_id: string;

  @property({
    type: 'string',
    required: true,
  })
  receiver_id: string;

  @property({
    type: 'string',
    required: true,
  })
  product_id: string;



  constructor(data?: Partial<Message>) {
    super(data);
  }
}

export interface MessageRelations {
  // describe navigational properties here
}

export type MessageWithRelations = Message & MessageRelations;
