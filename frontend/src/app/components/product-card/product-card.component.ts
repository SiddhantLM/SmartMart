import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { User } from 'src/app/models/user.model';
import { ProductService } from 'src/app/services/product.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit{
  @Input() product!: Product;
  @Input() isAddedToCart=false; //for disableing the add cart
  // @Output() wishlistToggled = new EventEmitter<Product>();
  @Output() addToCartEvent = new EventEmitter<Product>();
  user:User={}
  stars = Array(5).fill(0); 
  isLiked = false; 
  isInWishList=false;
  isWishListLoading=false;
  currentUserId:number=0

  private userSubscription?:Subscription;
  
  constructor(private router: Router,private productService:ProductService,private userStateService:UserStateService) {}
  ngOnInit(): void {
    this.userSubscription=this.userStateService.user$.subscribe(user=>{
      if(user){
        this.user=user;
        if(user.wishlist.find(i => i.productId === this.product.productId)) {
          this.isLiked = true;
        }
      }
    });
    console.log(this.product)
  }

  ngOnDestroy():void{
    this.userSubscription?.unsubscribe();
  }

  checkWishlistStatus(wishlist:Product[]):void{
    this.isInWishList=wishlist.some(p=>p.productId===this.product.productId);
  }

  toggleWishList(event:Event):void{
    event.stopPropagation();
    this.productService.addToWishlist(this.product.productId,this.user.userId).subscribe(data=>{
      this.userStateService.refreshUser();
      this.isLiked=!this.isLiked
    })
  }

  onCardClick(): void {
    this.router.navigate(['/product', this.product.productId]);
  }

  // toggleLike() {
  //   this.isLiked = !this.isLiked;
  // }

  onAddToCart($event: Event) {
    $event.stopPropagation(); // Prevent card click
    this.addToCartEvent.emit(this.product);
  }

  

  isStock(){
    if(this.product.stock<=0){
      return true;
    }
    return false;
  }
}
