import {Entity, model, property} from '@loopback/repository';

@model({
  settings: {
    postgresql: {
      table: 'message_img' 
    }
  }
})
export class MessageImg extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  _id?: string;

  @property({
    type: 'string',
    required: true,
  })
  image: string;

  @property({
    type: 'string',
    required: true,
  })
  message_id: string;

  constructor(data?: Partial<MessageImg>) {
    super(data);
  }
}


export interface MessageImgRelations {
  // describe navigational properties here
}

export type MessageImgWithRelations = MessageImg & MessageImgRelations;
