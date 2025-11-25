import {Entity, model, property} from '@loopback/repository';

@model()
export class product_Img extends Entity {
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
  product_id: string;


  constructor(data?: Partial<product_Img>) {
    super(data);
  }
}

export interface ProductImgRelations {
  // describe navigational properties here
}

export type ProductImgWithRelations = product_Img & ProductImgRelations;
