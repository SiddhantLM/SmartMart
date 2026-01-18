import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CartItem } from 'src/app/models/cart-item.model';
import { Cart } from 'src/app/models/cart.model';
import { Product } from 'src/app/models/product.model';
import { User } from 'src/app/models/user.model';
import { CartService } from 'src/app/services/cart.service';
import { UserStateService } from 'src/app/services/user-state.service';
 
@Component({
  selector: 'app-userviewwishlist',
  templateUrl: './userviewwishlist.component.html',
  styleUrls: ['./userviewwishlist.component.css']
})
export class UserviewwishlistComponent implements OnInit{
  showToast: boolean = false;
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'success';
 
  private userSub?: Subscription;
  user:User={}
  wishlist:Product[]=[]
  currentCart: Cart = {
    cartId: undefined,
    totalAmount: 0,
    quantity: 0,
    items: []
  };
 
  ngOnInit(): void {
    this.userSub = this.userState.user$.subscribe(user => {
      if (user) {
        this.user=user;
        this.wishlist=this.user.wishlist;
        if (user.cart) {
          this.currentCart = user.cart;
        }
      }
     
    });
  }
 
  constructor(private userState:UserStateService,private cartService:CartService){
 
  }
 
  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
  }
 
 
  onAddToCart(product: Product): void {
    // Validate cartId before proceeding
    if (!this.currentCart.cartId || this.currentCart.cartId === undefined) {    
      return;
    }
 
   
    // Check if product already exists in cart
    const existingItemIndex = this.currentCart.items?.findIndex(
      item => item.product?.productId === product.productId
    );
 
    if (existingItemIndex !== undefined && existingItemIndex !== -1 && this.currentCart.items) {
      // Product exists, increment quantity
      this.currentCart.items[existingItemIndex].quantity =
        (this.currentCart.items[existingItemIndex].quantity || 0) + 1;
    } else {
      // Product doesn't exist, add new item
      const newCartItem: CartItem = {
        product: product,
        quantity: 1
      };
     
      if (!this.currentCart.items) {
        this.currentCart.items = [];
      }
      this.currentCart.items.push(newCartItem);
    }
 
 
    // Call backend API to update cart
    const cartId = this.currentCart.cartId as number;
   
    console.log('Calling backend with cartId:', cartId);
   
    this.cartService.updateCart(cartId, this.currentCart).subscribe({
      next: (response: Cart) => {
        console.log('Cart updated successfully:', response);
        this.displayToast(`${product.name} added to cart successfully!`, 'success');
        this.userState.refreshUser();
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        this.displayToast('Failed to add product to cart', 'error');
 
      }
    });
  }
  isProductInCart(productId: number): boolean {
    return this.currentCart.items?.some(item => item.product.productId === productId) ?? false;
  }
 
  displayToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
 
    // Auto-hide toast after 3 seconds
    setTimeout(() => {
      this.showToast = false;
    }, 3000);
  }
 
 
}