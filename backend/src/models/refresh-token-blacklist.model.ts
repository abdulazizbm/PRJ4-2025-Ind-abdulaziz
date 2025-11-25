import {Entity, model, property} from '@loopback/repository';

@model({
  settings: {
    postgresql: {
      table: 'refresh_token_blacklist',
    },
  },
})
export class RefreshTokenBlacklist extends Entity {
  @property({
    type: 'string',
    id: true,
    generated: false,
    postgresql: {columnName: 'token'}, // optional, but explicit
  })
  token?: string;

  @property({
    type: 'date',
    postgresql: {columnName: 'blacklisted_at'}, // match your schema
  })
  createdAt?: string;

  constructor(data?: Partial<RefreshTokenBlacklist>) {
    super(data);
  }
}

export interface RefreshTokenBlacklistRelations {
  // describe navigational properties here
}

export type RefreshTokenBlacklistWithRelations = RefreshTokenBlacklist & RefreshTokenBlacklistRelations;
