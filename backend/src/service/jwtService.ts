import {injectable} from '@loopback/core';
import {HttpErrors} from '@loopback/rest';
import * as jwt from 'jsonwebtoken';
import {Users} from '../models';
import { repository } from '@loopback/repository';
import { RefreshTokenBlacklistRepository } from '../repositories/refresh-token-blacklist.repository';

@injectable()
export class Service {

  constructor(
    @repository(RefreshTokenBlacklistRepository)
    private blacklistRepo: RefreshTokenBlacklistRepository,
  ) {}

  private readonly jwtSecret = 'acc3ss_t0k3n*';
  private readonly jetSecretRefresh = 'new_Refr3sh!Ng_T0k3N!'

  async generateToken(user: Users): Promise<string> {
    const payload = {id: user._id, email: user.email};
    const token = jwt.sign(payload, this.jwtSecret, {expiresIn: '5m'})
    return token
  }

  async verifyToken(token: string): Promise<any> {
    console.log('Raw token:', token)
    try {
      const decoded = jwt.verify(token, this.jwtSecret) as any;
      console.log('Decoded JWT:', decoded);
      return {
        id: decoded.id,
        email: decoded.email      }
    } catch (error) {
      throw new HttpErrors[401]('token is invalid')
    }
  }

  async generateRefreshToken(user: Users): Promise<string> {
    const payload = {id: user._id, email: user.email};
    const refreshToken = jwt.sign(payload, this.jetSecretRefresh, {expiresIn: '7d'}); // Longer-lived refresh token
    return refreshToken;
  }

  async verifyRefreshToken(refreshToken: string): Promise<any> {
    try {
      const decoded = jwt.verify(refreshToken, this.jetSecretRefresh);
      return decoded;
    } catch (error) {
      throw new HttpErrors[401]('token is invalid')
    }
  }

  async invalidateRefreshToken(refreshToken: string): Promise<void> {
    await this.blacklistRepo.create({
      token: refreshToken,
      createdAt: new Date().toISOString(),
    });
  }

  async isRefreshTokenBlacklisted(refreshToken: string): Promise<boolean> {
    const token = await this.blacklistRepo.findById(refreshToken).catch(() => null);
    return !!token;
  }
}
