import { Order } from './order.model';
import { Product } from './product.model';

describe('Order Model', () => {

  fit('frontend_Order_model_should_create_an_instance', () => {
    const product: Product = {
        productId: 1,
        name: 'Chair',
        description: 'Comfortable wooden chair',
        price: 5000,
        stock: 10,
        category: 'Furniture',
        photoImage: 'image-url',
        createdAt: new Date(),
        updatedAt: new Date(),
        user: {
          userId: 1
        }
      };

    const order: Order = {
      orderId: 1,
      user: {
        userId: 2
      },
      product: [product],
      shippingAddress: '123 Street, City',
      totalAmount: 6000,
      quantity: 2,
      status: 'PENDING',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    expect(order).toBeTruthy();
    expect(order.orderId).toBeDefined();
    expect(order.shippingAddress).toBeDefined();
    expect(order.totalAmount).toBeDefined();
    expect(order.quantity).toBeDefined();
    expect(order.status).toBeDefined();
    expect(order.createdAt).toBeDefined();
    expect(order.updatedAt).toBeDefined();
  });

});
