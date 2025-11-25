import {Entity, model, property} from '@loopback/repository';

@model()
export class UserImg extends Entity {
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
  user_id: string;

  constructor(data?: Partial<UserImg>) {
    super(data);
  }
}

export interface UserImgRelations {
  // describe navigational properties here
}

export type UserImgWithRelations = UserImg & UserImgRelations;
