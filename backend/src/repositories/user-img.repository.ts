import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {DatabaseDataSource} from '../datasources';
import {UserImg, UserImgRelations} from '../models';

export class UserImgRepository extends DefaultCrudRepository<
  UserImg,
  typeof UserImg.prototype._id,
  UserImgRelations
> {
  constructor(
    @inject('datasources.database') dataSource: DatabaseDataSource,
  ) {
    super(UserImg, dataSource);
  }
}
