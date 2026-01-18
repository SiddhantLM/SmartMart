import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Router } from '@angular/router';
import { UserStateService } from './user-state.service';
import { environment  } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';


export const API_URL=environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

 
  constructor(private httpclient:HttpClient, private router:Router, private userState:UserStateService, private toastrService:ToastrService){

  }

  // register(user:User):Observable<any>{
  //   return this.httpclient.post(`${API_URL}/register`,user)
  // }

  public login(user: any): void {
    this.httpclient.post(`${API_URL}/login`, user).subscribe({
      next: (data: any) => {
        this.toastrService.success("Login Successful!");

        localStorage.setItem("jwtToken", data.token);
        localStorage.setItem("role", data.userRole);
        localStorage.setItem("username", data.userName);
        localStorage.setItem("user_id", data.userId);

        // Set cartId from user's cart
        if (user.cart?.cartId) {
          localStorage.setItem('cartId', user.cart?.cartId.toString());
          localStorage.setItem('cart', JSON.stringify(user.cart));
        }
        this.userState.userId = data.userId;

        if (data.userRole === "ADMIN") {
          this.router.navigate(['/admin/view-products']);
        } else if (data.userRole === "USER") {
          this.router.navigate(['/']);
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.toastrService.error("Invalid Credentials");
      }
    });
  }

  sendOtp(email: string): Observable<boolean> {
    return this.httpclient.post<boolean>(`${API_URL}/user/send-otp`, {email:email.trim().toLowerCase()});
  }

  registerWithOtp(user: User, otp: string): Observable<any> {
    const registerRequest = {
      user: user,
      otp: otp
    };

    return this.httpclient.post(`${API_URL}/register`, registerRequest);
  }

  isLoggedIn ():boolean {
    const token = localStorage.getItem('jwtToken');

    if(token != null) {
      return true;
    }

    return false;
  }

  logout () {
    localStorage.clear();
    this.toastrService.success("Logged Out Successfully!")
  }
}