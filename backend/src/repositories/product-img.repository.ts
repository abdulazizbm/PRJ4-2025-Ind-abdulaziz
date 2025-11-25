import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {DatabaseDataSource} from '../datasources';
import {product_Img, ProductImgRelations} from '../models';

export class ProductImgRepository extends DefaultCrudRepository<
product_Img,
  typeof product_Img.prototype._id,
  ProductImgRelations
> {
  constructor(
    @inject('datasources.database') dataSource: DatabaseDataSource,
  ) {
    super(product_Img, dataSource);
  }
}
