import {Entity, model, property} from '@loopback/repository';

@model()
export class Product extends Entity {
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
  seller_id: string;

  @property({
    type: 'string',
    required: true,
  })
  title: string;

  @property({
    type: 'string',
  })
  description?: string;

  @property({
    type: 'number',
    required: true,
  })
  price: number;

  @property({
    type: 'string',
    // required: true,
    default: 'available',
  })
  status?: string;

  @property({
    type: 'date',
  })
  created_at?: string;

  @property({
    type: 'string',
    required: true,
  })
  category_id?: string;    //made category_id, product_id, seller_id, status, optional for noe


  constructor(data?: Partial<Product>) {
    super(data);
  }
}

export interface ProductRelations {
  // describe navigational properties here
}

export type ProductWithRelations = Product & ProductRelations;
