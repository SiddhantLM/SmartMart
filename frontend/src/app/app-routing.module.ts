import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminViewOrdersComponent } from './components/adminvieworders/adminvieworders.component';
import { AdminaddproductComponent } from './components/adminaddproduct/adminaddproduct.component';
import { AdminviewproductComponent } from './components/adminviewproduct/adminviewproduct.component';
import { AdminViewUserDetailsComponent } from './components/adminviewuserdetails/adminviewuserdetails.component';
import { AdminviewfeedbackComponent } from './components/adminviewfeedback/adminviewfeedback.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { UserviewproductComponent } from './components/userviewproduct/userviewproduct.component';
import { ErrorComponent } from './components/error/error.component';
import { UserviewordersComponent } from './components/uservieworders/uservieworders.component';
import { UserCartComponent } from './user-cart/user-cart.component';
import { UseraddfeedbackComponent } from './components/useraddfeedback/useraddfeedback.component';
import { OtpComponent } from './components/otp/otp.component';
import { UserviewproductlistComponent } from './components/userviewproductlist/userviewproductlist.component';
import { UserviewcartComponent } from './components/userviewcart/userviewcart.component';
import { adminGuard } from './guards/admin.guard';
import { userGuard } from './guards/user.guard';
import { loginGuard } from './guards/login.guard';
import { UserviewwishlistComponent } from './components/userviewwishlist/userviewwishlist.component';
import { AdminviewstockreminderComponent } from './components/adminviewstockreminder/adminviewstockreminder.component';

 
const routes: Routes = [
  {path:'home',component:HomeComponent},
  { path: 'admin/add-product', component: AdminaddproductComponent,canActivate:[adminGuard]},
  { path: 'admin/view-products', component: AdminviewproductComponent,canActivate:[adminGuard] },
  { path: 'admin/view-orders', component: AdminViewOrdersComponent,canActivate:[adminGuard] },
  { path: 'admin/view-users', component: AdminViewUserDetailsComponent,canActivate:[adminGuard] },
  { path: 'admin/view-feedbacks', component:AdminviewfeedbackComponent,canActivate:[adminGuard] },
  {path:'admin/view-stockreminder',component:AdminviewstockreminderComponent,canActivate:[adminGuard]},
  { path: 'login', component: LoginComponent, canActivate:[loginGuard] },
  { path: 'register', component: RegistrationComponent, canActivate:[loginGuard] },
  {path: 'otp',component:OtpComponent, canActivate:[loginGuard]},
  { path: '', component: HomeComponent },
  { path: 'user/view-products', component:UserviewproductlistComponent,canActivate:[userGuard]},
  { path:"product/:id", component:UserviewproductComponent,canActivate:[userGuard] },
  { path: 'user/view-orders', component: UserviewordersComponent,canActivate:[userGuard] },
  { path: 'user/cart', component: UserviewcartComponent,canActivate:[userGuard] },
  { path: 'user/feedback', component:UseraddfeedbackComponent,canActivate:[userGuard] },
  { path: 'otp-verify', component:OtpComponent },
  { path:"error", component:ErrorComponent },
  {path:"user/view-wishlist",component:UserviewwishlistComponent,canActivate:[userGuard]}
  
];
 

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
