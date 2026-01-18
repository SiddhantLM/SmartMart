import { Injectable } from '@angular/core';
import { Cart } from '../models/cart.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl=`${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) { }

  updateCart(cartId: Number, cart: Cart): Observable<Cart> {
    return this.http.post<Cart>(`${this.apiUrl}/${cartId}`, cart);
  }

  resetCart(cartId: Number): Observable<boolean> {
    return this.http.put<boolean>(`${this.apiUrl}/${cartId}/reset`, null);
  }

  removeItem(cartId:Number, itemId:Number):Observable<Cart> {
    return this.http.put<Cart>(`${this.apiUrl}/${cartId}/item/${itemId}`, {});
  }
}
