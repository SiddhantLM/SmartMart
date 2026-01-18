import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
 
@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
user: User = {};
otpSent = false;
loading = false;
registrationFailed:boolean = false;

constructor(

  private authService: AuthService,

  private router: Router

) {}

sendOtp() {

  if (!this.user.email) {

    alert('Email is required');

    return;

  }

  this.loading = true;

  // 1. Save user temporarily

  localStorage.setItem('pendingUser', JSON.stringify(this.user));

  // 2. Send OTP

  this.authService.sendOtp(this.user.email).subscribe({

    next: (success) => {

      // if (success) {

        this.otpSent = true;

        this.loading = false;

        console.log(success)

        alert('OTP sent successfully!');

        this.goToOtp();

      // }

    },

    error: (err) => {

      this.loading = false;

      this.registrationFailed = true;

      console.log(err)

      //alert('Failed to send OTP');

    }

  });

}

onSubmit (form:NgForm) {

  //IMPLEMENT

  if(form.valid) {

    this.sendOtp();

  }

}

goToOtp() {

  this.router.navigate(['/otp-verify']);

}
onlyNumbers(event: KeyboardEvent): boolean {
  const charCode = event.which ? event.which : event.keyCode;
  // Only allow numbers (0-9)
  if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    event.preventDefault();
    return false;
  }
  return true;
}

}
 