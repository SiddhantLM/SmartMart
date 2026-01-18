import { Product } from './product.model';

describe('Product Model', () => {

  fit('frontend_Product_model_should_create_an_instance', () => {
    const product: Product = {
        productId: 1,
        name: 'Table',
        description: 'A sturdy wooden table',
        price: 15000,
        stock: 20,
        category: 'Furniture',
        photoImage: 'image-url',
        createdAt: new Date(),
        updatedAt: new Date(),
        user: {
          userId: 1
        }
      };

    expect(product).toBeTruthy();
    expect(product).toBeDefined();
    expect(product.productId).toBeDefined();
    expect(product.name).toBeDefined();
    expect(product.description).toBeDefined();
    expect(product.price).toBeDefined();
    expect(product.stock).toBeDefined();
    expect(product.category).toBeDefined();
    expect(product.createdAt).toBeDefined();
    expect(product.updatedAt).toBeDefined();
    expect(product.user).toBeDefined();
  });

});
