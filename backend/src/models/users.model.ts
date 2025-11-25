import {Entity, model, property} from '@loopback/repository';

@model()
export class Users extends Entity {
  // Use MongoDB’s ObjectId string as the PK
  @property({
    type: 'string',
    id: true,
    generated: false,        // Cosmos will generate it for you
  })
  _id?: string;

  @property({
    type: 'string',
    required: true,
  })
  username: string;

  @property({
    type: 'string',
    required: true,
  })
  email: string;

  @property({
    type: 'string',
    required: true,
  })
  password: string;

  constructor(data?: Partial<Users>) {
    super(data);
  }
}

export interface UsersRelations {
  // navigational properties here
}

export type UsersWithRelations = Users & UsersRelations;
