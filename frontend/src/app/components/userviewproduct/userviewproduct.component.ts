import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';
import { CartItem } from 'src/app/models/cart-item.model';
import { Cart } from 'src/app/models/cart.model';
import { Product } from 'src/app/models/product.model';
import { Review } from 'src/app/models/review.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
import { StockReminderService } from 'src/app/services/stock-reminder.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-userviewproduct',
  templateUrl: './userviewproduct.component.html',
  styleUrls: ['./userviewproduct.component.css']
})
export class UserviewproductComponent {
  product: Product={};
  reviews: Review[] = [];
  averageRating = 0;
  stars = [1, 2, 3, 4, 5];
  user:User={}
  summary:String;
  summaryLoading:boolean=false;
 
  selectedQty = 1;
  email = '';
 
  // Review form
  newReview: Review = { rating: 0, content: '' };
  reviewFormVisible = false;
 
  // Toast
  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
 
  private userSub?: Subscription;
 
  currentUserId = 0;
 
  // Cart management (same as userviewproductlist)
  currentCart: Cart = {
    cartId: undefined,
    totalAmount: 0,
    quantity: 0,
    items: []
  };
  isLiked:boolean=false;
 
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService,
    private userState: UserStateService,
    private stockReminderService:StockReminderService
  ) {}
 
  ngOnInit(): void {
    // Initialize cart ID first
 
    // Subscribe to user state
    this.userSub = this.userState.user$.subscribe(user => {
      if (user) {
        this.currentUserId = user.userId || Number(localStorage.getItem('user_id')) || 0;
        this.user=user;
        if(user.wishlist.find(i => i.productId === this.product.productId)) {
          this.isLiked = true;
        }
       
        // Set cartId from user's cart
        if (user.cart) {
          this.currentCart = user.cart;
        }
       
      
        this.loadProduct()
        
      }
    });
    this.loadReviewSummary();
   
  }
 
  loadProduct(){
    this.route.paramMap
      .pipe(
        switchMap(params => {
          const id = Number(params.get('id'));
          return this.productService.getProductById(id);
        })
      )
      .subscribe({
        next: (product: Product) => {
          this.product = product;
          // Set placeholder image if no image exists
          if (!this.product.photoImage) {
            this.product.photoImage = 'https://via.placeholder.com/500x500/f5f3ef/1a1a1a?text=Product+Image';
          }
          this.reviews = product.reviews || [];
          this.calculateAverageRating();
          this.loadReviewSummary();
        },
        error: () => this.showToastMsg('Failed to load product', 'error')
      });
  }
 
  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
  }
 
  loadReviewSummary(){
    if (!this.reviews || this.reviews.length === 0) {
      this.summary = 'No reviews yet.';
      this.summaryLoading = false;
      return;
    }
    
    this.summaryLoading = true;
    this.summary = '';
    
    this.productService.getSummary(this.reviews).subscribe({
      next: (response: any) => {
        console.log('Summary response:', response); // Debug log
        this.summary = response.summary || response || 'No summary available';
        this.summaryLoading = false;
      },
      error: (error) => {
        console.log('Summary error:', error);
        this.summary = 'Failed to generate summary';
        this.summaryLoading = false;
      }
    });
  }
 

  calculateAverageRating(): void {
    if (!this.reviews.length) {
      this.averageRating = 0;
      return;
    }
    const sum = this.reviews.reduce((acc, r) => acc + (r.rating || 0), 0);
    this.averageRating = Number((sum / this.reviews.length).toFixed(1));
  }
 
  addToCart(): void {
    if (!this.product || this.product.stock === 0) {
      this.showToastMsg('Product is out of stock', 'error');
      return;
    }
 
    // Validate quantity
    if (this.selectedQty < 1 || this.selectedQty > this.product.stock) {
      this.showToastMsg('Invalid quantity selected', 'error');
      return;
    }
 
    // Validate cartId before proceeding
    if (!this.currentCart.cartId || this.currentCart.cartId === undefined) {
      console.error('Cart ID is undefined or invalid');
      console.log('Attempting to reinitialize cartId...');
     
      // Try to reinitialize
 
   
    }
 
 
   
    // Check if product already exists in cart
    const existingItemIndex = this.currentCart.items?.findIndex(
      item => item.product?.productId === this.product.productId
    );
 
    if (existingItemIndex !== undefined && existingItemIndex !== -1 && this.currentCart.items) {
      // Product exists, increment quantity by selectedQty
      this.currentCart.items[existingItemIndex].quantity =
        (this.currentCart.items[existingItemIndex].quantity || 0) + this.selectedQty;
    } else {
      // Product doesn't exist, add new item with selectedQty
      const newCartItem: CartItem = {
        product: this.product,
        quantity: this.selectedQty
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
        // Update local cart with response from backend (backend calculates totals)
        this.currentCart = response;
        this.showToastMsg(`${this.product.name} (×${this.selectedQty}) added to cart!`, 'success');
        // Reset quantity selector to 1
        this.selectedQty = 1;
        this.userState.refreshUser();
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        this.showToastMsg('Failed to add product to cart', 'error');
      }
    });
  }
 
 
  addToWishlist(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
 
    this.productService.addToWishlist(this.product.productId!, this.currentUserId).subscribe({
      next: (success) => {
        if (success) {
          this.showToastMsg('Added to wishlist', 'success');
        } else {
          this.showToastMsg('Removed from wishlist', 'success');
        }
      },
      error: () => this.showToastMsg('Wishlist update failed', 'error')
    });
  }
 
  submitReview(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
 
    if (!this.newReview.rating || !this.newReview.content?.trim()) {
      this.showToastMsg('Please provide rating and comment', 'error');
      return;
    }
 
    this.productService
      .addReview(this.product.productId!, this.currentUserId, this.newReview)
      .subscribe({
        next: (newreview) => {
          // Backend returns full product with new review
          this.loadProduct();
          this.newReview = { rating: 0, content: '' };
          this.reviewFormVisible = false;
          this.showToastMsg('Thank you! Review added.', 'success');
        },
        error: () => this.showToastMsg('Failed to submit review', 'error')
      });
  }
 
  notifyMe(): void {
    this.stockReminderService.addReminder(this.product.productId,this.email).subscribe({
      next: (data)=>{
      console.log('login reminder sent');
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }
 
  showToastMsg(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => (this.showToast = false), 4000);
  }
 
  toggleWishList(event:Event):void{
    event.stopPropagation();
    this.productService.addToWishlist(this.product.productId,this.user.userId).subscribe(data=>{
      this.userState.refreshUser();
    })
  }
}



