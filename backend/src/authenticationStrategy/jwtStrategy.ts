import {AuthenticationStrategy} from '@loopback/authentication';
import {inject, injectable} from '@loopback/core';
import {HttpErrors, Request} from '@loopback/rest';
import {securityId, UserProfile} from '@loopback/security';
import {Service} from '../service/jwtService';

@injectable()
export class JWTStrategy implements AuthenticationStrategy {
  name = 'jwt';

  constructor(
    @inject('services.jwtService')
    public jwtService: Service,
  ) {}

  async authenticate(request: Request): Promise<UserProfile | undefined> {
    const token: string = this.extractCredentials(request);
    const user = await this.jwtService.verifyToken(token); 

    if (!user?.id) {
      throw new HttpErrors.Unauthorized('Invalid token: no user ID');
    }

    const userProfile: UserProfile = {
      [securityId]: user.id,
      id: user.id,
      email: user.email,
    };

    return userProfile;
  }
  extractCredentials(request: Request): string {
    if (!request.headers.authorization) {
      throw new Error('Authorization header is missing');
    }

    const authHeaderValue = request.headers.authorization;
    if (!authHeaderValue.startsWith('Bearer')) {
      throw new Error('Authorization header is not of type Bearer');
    }

    const parts = authHeaderValue.split(' ');
    if (parts.length !== 2) {
      throw new Error('Authorization header value has too many parts');
    }

    const token = parts[1];
    return token;
  }
}