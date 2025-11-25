import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {DatabaseDataSource} from '../datasources';
import {MessageImg, MessageImgRelations} from '../models';

export class MessageImgRepository extends DefaultCrudRepository<
  MessageImg,
  typeof MessageImg.prototype._id,
  MessageImgRelations
> {
  constructor(
    @inject('datasources.database') dataSource: DatabaseDataSource,
  ) {
    super(MessageImg, dataSource);
  }
  
}
