import {Entity, model, property} from '@loopback/repository';

@model()
export class Review extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  _id?: string;

  @property({
    type: 'number',
    required: true,
  })
  rating: number;

  @property({
    type: 'string',
  })
  comment?: string;

  @property({
    type: 'date',
  })
  created_at?: string;

  @property({
    type: 'string',
    required: true,
  })
  reviewer_id: string;

  @property({
    type: 'string',
    required: true,
  })
  reviewee_id: string;


  constructor(data?: Partial<Review>) {
    super(data);
  }
}

export interface ReviewRelations {
  // describe navigational properties here
}

export type ReviewWithRelations = Review & ReviewRelations;
