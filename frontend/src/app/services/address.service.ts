import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Address } from '../models/address.model';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AddressService {

  // Update this URL to match your backend base URL
  private apiUrl = `${environment.apiUrl}/address`; // Change port if needed

  constructor(private http: HttpClient) { }

  // POST: Add new address for a user
  addAddress(userId: Number, address: Address): Observable<Address> {
    return this.http.post<Address>(`${this.apiUrl}/user/${userId}`, address)
  }

  // PUT: Update existing address by addressId
  updateAddress(addressId: Number, address: Address): Observable<Address> {
    return this.http.put<Address>(`${this.apiUrl}/${addressId}`, address)
  }

  // DELETE: Delete address by addressId
  deleteAddress(addressId: Number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.apiUrl}/${addressId}`)
  }

}