import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
@Injectable({ providedIn: 'root' })
export class RazorpayService {
  apiUrl = environment.apiUrl
  
  constructor(private http: HttpClient) {}


  createOrder(amount: number) {
    // amount in rupees (we convert on backend)
    return this.http.post<any>(`${this.apiUrl}/razorpay/createOrder`, { amount });
  }

  verifyPayment(payload: any) {
    return this.http.post<any>(`${this.apiUrl}/razorpay/verifyPayment`, payload);
  }
}