import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StockReminderService {
  apiUrl = `${environment.apiUrl}/stock-reminder`
  constructor(private http:HttpClient) { }

  addReminder (productId:Number, email:string):Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/product/${productId}`, {email:email});
  }

  sendReminder (reminderId:Number):Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/${reminderId}`, {});
  }

  getReminder():Observable<any>{
    return this.http.get(`${this.apiUrl}`)
  }

}
