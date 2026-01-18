import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Address } from 'src/app/models/address.model';
import { CartItem } from 'src/app/models/cart-item.model';
import { Cart } from 'src/app/models/cart.model';
import { User } from 'src/app/models/user.model';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { RazorpayService } from 'src/app/services/razorpay.service';
import { UserStateService } from 'src/app/services/user-state.service';
import { environment } from 'src/environments/environment';
declare var Razorpay: any;

@Component({
  selector: 'app-userviewcart',
  templateUrl: './userviewcart.component.html',
  styleUrls: ['./userviewcart.component.css']
})
export class UserviewcartComponent implements OnInit {

  cart: Cart = {};
  isLoggedIn:boolean = false;
  shippingAddress:Address = null;
  addresses:Address[] = [];
  user:User = {}

  constructor(private userState:UserStateService,private cartService:CartService, private razorpayService:RazorpayService, private toastrService:ToastrService, private orderService:OrderService) {}

  ngOnInit(): void {
    this.userState.user$.subscribe(
      user => {
        if(user) {
          this.isLoggedIn = true;
          this.user = user;
          this.cart = user.cart;
          this.addresses = user.addresses
        }
      }
    )
  }

  addressSelected (address:Address) {
    this.shippingAddress = address;
  }

  increaseQty(item: CartItem) {
    if (item.quantity < (item.product?.stock || 0)) {
      this.cart.items.find(i => i.id === item.id).quantity++;
      this.cartService.updateCart(this.cart.cartId, this.cart).subscribe(data => {
        this.cart = data;
      })
    }
  }

  decreaseQty(item: CartItem) {
    if (item.quantity > 1) {
      this.cart.items.find(i => i.id === item.id).quantity--;
      this.cartService.updateCart(this.cart.cartId, this.cart).subscribe(data => {
        this.cart = data;
        this.userState.refreshUser();
      })
    } else if (item.quantity === 1) {
      this.removeItem(item);
      this.userState.refreshUser();
    }
  }

  removeItem(item: CartItem) {
    this.cartService.removeItem(this.cart.cartId, item.id).subscribe(data => {
      this.cart = data;
      this.userState.refreshUser();
    })
  }

  placeOrder() {
    
    if(!this.shippingAddress) {
      this.toastrService.warning("Select a shipping address");
      return;
    }

    this.razorpayService.createOrder(this.cart.totalAmount).subscribe({
      next:order => {
        const options = {
          key: environment.RAZORPAY_KEY,
          amount: order.amount, // amount in paise from backend
          currency: order.currency,
          name: 'My App',
          description: 'Test payment',
          order_id: order.id,
          handler: (response: any) => this.onPaymentSuccess(response),
          prefill: {
            name: 'Test User',
            email: 'test@example.com',
            contact: '9999999999'
          },
          theme: { color: '#3399cc' }
        };
        const rzp = new Razorpay(options);
        rzp.open();
      },
      error:(err) => {
        console.log(err);
        this.toastrService.error("Error while creating payment");
      }
    });
  }

  onPaymentSuccess(response: any) {
    // response has: razorpay_payment_id, razorpay_order_id, razorpay_signature
    this.razorpayService.verifyPayment(response).subscribe({
      next:res => {
        if (res.valid) {
          // API CALL TO /api/add-order
          this.orderService.placeOrder({userId:this.user.userId,cartId:this.cart.cartId, addressId:this.shippingAddress.addressId}).subscribe({
            next:(data) => {
              this.toastrService.success("Order placed successfully");
              this.userState.refreshUser();
            },
            error:(err) => {
              console.log(err);
              this.toastrService.error("Error adding order to user");
            }
          });
  
          console.log(res);
          alert('Payment verified successfully (test).');
        } else {
          alert('Payment verification failed!');
        }
      },
      error:(err) => {
        console.log(err);
        this.toastrService.error("Error while verifying payment");
      }
    });
  }
}
