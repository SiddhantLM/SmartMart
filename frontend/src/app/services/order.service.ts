import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../models/order.model';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})

export class OrderService {

  private apiUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  placeOrder(order: {userId:Number, cartId:Number, addressId:Number}): Observable<Order> {
    console.log(order);
    return this.http.post<Order>(`${this.apiUrl}/user/${order.userId}/cart/${order.cartId}`, {addressId:order.addressId});
  }
  //
  getUserOrders(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/user/${userId}`);
  }

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}`);
  }

  updateOrderStatus(orderId: number, status: string): Observable<any> {
    console.log(status)
    return this.http.put(`${this.apiUrl}/${orderId}/status`, { status:status });
  }

}
