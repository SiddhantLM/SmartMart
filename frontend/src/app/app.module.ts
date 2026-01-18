import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminaddproductComponent } from './components/adminaddproduct/adminaddproduct.component';
import { AdminnavbarComponent } from './components/adminnavbar/adminnavbar.component';
import { AdminviewfeedbackComponent } from './components/adminviewfeedback/adminviewfeedback.component';
import { AdminviewproductComponent } from './components/adminviewproduct/adminviewproduct.component';
import { ErrorComponent } from './components/error/error.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { UseraddfeedbackComponent } from './components/useraddfeedback/useraddfeedback.component';
import { UsernavbarComponent } from './components/usernavbar/usernavbar.component';
import { UserviewfeedbackComponent } from './components/userviewfeedback/userviewfeedback.component';
import { UserviewproductComponent } from './components/userviewproduct/userviewproduct.component';
import { FormsModule } from '@angular/forms';
import { UserviewordersComponent } from './components/uservieworders/uservieworders.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AdminViewUserDetailsComponent } from './components/adminviewuserdetails/adminviewuserdetails.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProductCardComponent } from './components/product-card/product-card.component';
import { ReactiveFormsModule } from '@angular/forms';
// import { BrowserModule } from '@angular/platform-browser';
import { OtpComponent } from './components/otp/otp.component';
import { UserviewproductlistComponent } from './components/userviewproductlist/userviewproductlist.component';
import { UserCartComponent } from './user-cart/user-cart.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AdminViewOrdersComponent } from './components/adminvieworders/adminvieworders.component';
import { BrowserModule } from '@angular/platform-browser';
import { UserviewcartComponent } from './components/userviewcart/userviewcart.component';
import { UseraddressComponent } from './components/useraddress/useraddress.component';
import { ToastrModule } from 'ngx-toastr';
import { UserviewwishlistComponent } from './components/userviewwishlist/userviewwishlist.component';
import { AdminviewstockreminderComponent } from './components/adminviewstockreminder/adminviewstockreminder.component';


@NgModule({
  declarations: [
    AppComponent,
    AdminaddproductComponent,
    AdminnavbarComponent,
    AdminviewfeedbackComponent,
    AdminviewproductComponent,
    AdminViewUserDetailsComponent,
    AdminViewOrdersComponent,
    ErrorComponent,
    HomeComponent,
    NavbarComponent,
    RegistrationComponent,
    UseraddfeedbackComponent,
    UsernavbarComponent,
    UserviewfeedbackComponent,
    UserviewordersComponent,
    UserviewproductComponent,
    LoginComponent,
    ProductCardComponent,
    OtpComponent,
    UserCartComponent,
    UserviewcartComponent,
    UserviewcartComponent,
    UserviewproductlistComponent,
    UserCartComponent,
    UseraddressComponent,
    UserviewwishlistComponent,
    AdminviewstockreminderComponent,
    
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule, 
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    ToastrModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
