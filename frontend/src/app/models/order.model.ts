import { Address } from './address.model';
import { OrderItem } from './order-item.model';

export interface Order {
  orderId?: number;
  items: OrderItem[];
  address: Address;
  totalAmount: number;
  quantity: number;
  status?: string;
  createdAt?: Date;
  updatedAt?: Date;
}