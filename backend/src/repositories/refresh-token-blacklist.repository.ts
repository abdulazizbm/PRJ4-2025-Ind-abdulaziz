import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {DatabaseDataSource} from '../datasources';
import {RefreshTokenBlacklist, RefreshTokenBlacklistRelations} from '../models';

export class RefreshTokenBlacklistRepository extends DefaultCrudRepository<
  RefreshTokenBlacklist,
  typeof RefreshTokenBlacklist.prototype.token,
  RefreshTokenBlacklistRelations
> {
  constructor(
    @inject('datasources.database') dataSource: DatabaseDataSource,
  ) {
    super(RefreshTokenBlacklist, dataSource);
  }
}
