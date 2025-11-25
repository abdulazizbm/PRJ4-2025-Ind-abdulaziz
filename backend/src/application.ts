import {AuthenticationComponent, registerAuthenticationStrategy} from '@loopback/authentication';
import {BootMixin} from '@loopback/boot';
import {ApplicationConfig} from '@loopback/core';
import {SecuritySchemeObject} from '@loopback/openapi-v3';
import {RepositoryMixin} from '@loopback/repository';
import {RestApplication} from '@loopback/rest';
import {
  RestExplorerBindings,
  RestExplorerComponent,
} from '@loopback/rest-explorer';
import {ServiceMixin} from '@loopback/service-proxy';
import path from 'path';
import {JWTStrategy} from './authenticationStrategy/jwtStrategy';
import {MySequence} from './sequence';
import {Service as JwtService} from './service/jwtService';
import {StripeService} from './service/StripeService';

export {ApplicationConfig};

export class BackendApplication extends BootMixin(
  ServiceMixin(RepositoryMixin(RestApplication)),
) {
  expressApp: any;

  constructor(options: ApplicationConfig = {}) {
    super(options);

    // Bind services
    this.bind('services.StripeService').toClass(StripeService);
    this.bind('services.jwtService').toClass(JwtService);

    // CORS configuration
    this.configureCors();

    // Register authentication component and strategy
    this.component(AuthenticationComponent);
    registerAuthenticationStrategy(this, JWTStrategy);

    // Custom request sequence
    this.sequence(MySequence);

    // Static content
    this.static('/', path.join(__dirname, '../public'));

    // Configure the REST API explorer
    this.configure(RestExplorerBindings.COMPONENT).to({
      path: '/explorer',
    });
    this.component(RestExplorerComponent);

    // Register OpenAPI JWT security scheme for the Explorer
    const JWT_SECURITY_SCHEME: SecuritySchemeObject = {
      type: 'http',
      scheme: 'bearer',
      bearerFormat: 'JWT',
    };

    this.api({
      openapi: '3.0.0',
      info: {
        title: 'Student Marketplace API',
        version: '1.0.0',
      },
      paths: {},
      components: {
        securitySchemes: {
          jwt: JWT_SECURITY_SCHEME,
        },
      },
      security: [
        {
          jwt: [],
        },
      ],
    });

    // Set up boot options for auto-discovery
    this.projectRoot = __dirname;
    this.bootOptions = {
      controllers: {
        dirs: ['controllers'],
        extensions: ['.controller.js'],
        nested: true,
      },
    };
  }

  private configureCors() {
    this.bind('rest.cors.options').to({
      origin: '*',
      methods: 'GET,POST,PUT,DELETE',
      allowedHeaders: 'Content-Type, Authorization',
    });
  }

  // Uncomment if you want to support raw body parsing for Stripe webhooks
  // async setupStripeWebhookMiddleware() {
  //   const restServer = await this.getServer(RestServer);
  //   await restServer.expressMiddleware(
  //     '/stripe/webhook',
  //     express.raw({type: 'application/json'}),
  //   );
  // }
}
