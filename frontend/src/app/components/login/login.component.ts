import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { Login } from 'src/app/models/login.model';
import { ToastrService } from 'ngx-toastr';
 
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  
  loginData = { username: '', password: '' };
  loginFailed:boolean = false;

  constructor(private authService: AuthService, private toasterService:ToastrService) {}

  onLogin() {
    this.authService.login(this.loginData);
  }

  onSubmit(form:NgForm) {
    if(form.valid) {
      this.authService.login({username:this.loginData.username, password:this.loginData.password});
    }
  }
  
}
 