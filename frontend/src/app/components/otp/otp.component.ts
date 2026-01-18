import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css']
})
export class OtpComponent implements OnDestroy {

  otp: string = '';
  email: string = '';
  pendingUser: User;
  otpArray: String[] = ["", "", "", "", "", ""];
  
  // Timer properties
  timer: number = 60; // 60 seconds countdown
  timerInterval: any;
 
  constructor(private authService: AuthService, private router: Router,private toastrservice:ToastrService) {}
 
  ngOnInit(): void {
    this.pendingUser = JSON.parse(localStorage.getItem('pendingUser'));
    this.email = this.pendingUser?.email || '';
    
    // Start the timer when component loads
    this.startTimer();
  }

  ngOnDestroy(): void {
    // Clear the timer interval when component is destroyed
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  startTimer(): void {
    this.timer = 60; // Reset to 60 seconds
    
    // Clear any existing interval
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }

    // Start countdown
    this.timerInterval = setInterval(() => {
      if (this.timer > 0) {
        this.timer--;
      } else {
        clearInterval(this.timerInterval);
      }
    }, 1000);
  }
 
  verifyOtp() {
    this.otp = this.otpArray.join("");
    if (!this.otp || this.otp.length !== 6 || !/^\d+$/.test(this.otp)) {
      //alert('Please enter a valid 6-digit OTP');
      this.toastrservice.error("Please enter a valid 6-digit OTP")
      return;
    }
 
    if (!this.pendingUser) {
      //alert('User data not found');
      this.toastrservice.error("User data not found")
      return;
    }
    
    // Direct call – no payload object in component
    this.authService.registerWithOtp(this.pendingUser, this.otp.trim()).subscribe({
      next: (res) => {
        // alert('Registration successful! Please login.');
        this.toastrservice.success("Registration successful! Please login.")
        localStorage.removeItem('pendingUser');
        
        // Clear timer on successful verification
        if (this.timerInterval) {
          clearInterval(this.timerInterval);
        }
        
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration failed', err);
        this.toastrservice.error("Invalid or expired OTP. Please try again.")
        //alert('Invalid or expired OTP. Please try again.');
      }
    });
  }
 
  movePrev(event: any, index: number) {
    if (event.key === 'Backspace' && index > 0 && !this.otpArray[index]) {
      const prevInput = event.target.previousElementSibling;
      if (prevInput) {
        prevInput.focus();
      }
    }
  }
 
  moveNext(event: any, index: number) {
    const input = event.target.value;
    if (input && index < this.otpArray.length - 1) {
      const nextInput = event.target.nextElementSibling;
      if (nextInput) {
        nextInput.focus();
      }
    }
  }
 
  resendOtp() {
    // Prevent resending if timer is still running
    if (this.timer > 0) {
      return;
    }

    this.authService.sendOtp(this.email).subscribe({
      next: () => {
        alert('OTP resent!');
        this.toastrservice.info("OTP resent!")
        // Clear OTP inputs
        this.otpArray = ["", "", "", "", "", ""];
        // Restart the timer
        this.startTimer();
      },
      error: () => {
        this.toastrservice.error("Failed to resend OTP")
        //alert('Failed to resend OTP');
      }
    });
  }
}