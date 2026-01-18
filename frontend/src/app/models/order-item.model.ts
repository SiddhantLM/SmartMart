import { Product } from "./product.model";

export interface OrderItem {
    id?: number;            
    orderId?: number;       
    product: Product;       
    quantity: number;
  }
  