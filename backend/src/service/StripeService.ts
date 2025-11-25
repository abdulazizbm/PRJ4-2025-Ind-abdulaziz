import {injectable, /*inject*/} from '@loopback/core';
import Stripe from 'stripe';
import {repository} from '@loopback/repository';
import {TransactionRepository, UsersRepository} from '../repositories';

@injectable()
export class StripeService {
  private stripe: Stripe;

  constructor(
    @repository(UsersRepository)
    private usersRepository: UsersRepository,

    @repository(TransactionRepository)
    private transactionRepository: TransactionRepository,
  ) {
    const key = process.env.STRIPE_SECRET_KEY;
    if (!key) {
      throw new Error('Stripe secret key is not set');
    }
    this.stripe = new Stripe(key, {
      apiVersion: '2025-05-28.basil',
    });
  }

  async createCheckoutSession(
    amount: number,
    currency: string,
    sellerId: string,    // now a string ObjectId
    productId: string,   // string
    buyerId: string,     // string
  ) {
    // findById takes a string, not number
    const seller = await this.usersRepository.findById(sellerId);
    if (!seller?.email) {
      throw new Error('Seller does not have a valid email.');
    }

    const session = await this.stripe.checkout.sessions.create({
      payment_method_types: ['card'],
      mode: 'payment',
      line_items: [{
        price_data: {
          currency,
          unit_amount: Math.round(amount * 100),
          product_data: { name: `Product ${productId}` },
        },
        quantity: 1,
      }],
      customer_email: seller.email,
      success_url: `https://yourdomain.com/payment-success?session_id={CHECKOUT_SESSION_ID}`,
      cancel_url: `https://yourdomain.com/payment-cancelled`,
      metadata: {
        buyerId,
        sellerId,
        productId,
      },
    });

    return { sessionId: session.id, url: session.url };
  }

  async handleWebhook(payload: Buffer, sig: string) {
    const event = this.stripe.webhooks.constructEvent(
      payload,
      sig,
      process.env.STRIPE_WEBHOOK_SECRET!,
    );

    if (event.type === 'checkout.session.completed') {
      const session = event.data.object as Stripe.Checkout.Session;
      const { buyer_id, seller_id, product_id } = session.metadata!;

      // Create transaction with string IDs
      await this.transactionRepository.create({
        buyer_id,
        seller_id,
        product_id,
        transaction_date: new Date().toISOString(),
      });
    }

    return { received: true };
  }
}
