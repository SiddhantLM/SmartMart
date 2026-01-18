import { Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { CartItem } from 'src/app/models/cart-item.model';
import { Cart } from 'src/app/models/cart.model';
import { Product } from 'src/app/models/product.model';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
import { UserStateService } from 'src/app/services/user-state.service';

export interface ProductFilter {
  sortOrder?: 'lowToHigh' | 'highToLow';
  categories?: string[];
  minRating?: number; // 3|4|5
}
 
@Component({
  selector: 'app-userviewproductlist',
  templateUrl: './userviewproductlist.component.html',
  styleUrls: ['./userviewproductlist.component.css']
})
export class UserviewproductlistComponent implements OnDestroy {
  products: Product[] = [];
  isLoading: boolean = false;
  currentUserId: number = 0;
  currentCart: Cart = {
    cartId: undefined,
    totalAmount: 0,
    quantity: 0,
    items: []
  };

  // PAGINATION

  // filteredProducts: Product[] = [];
  paginatedProducts: Product[] = [];  // ← Now used in template

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 12;
  totalPages: number = 1;
  pageNumbers: number[] = [];

  filteredProducts: Product[] = [];
  showFilterModal = false;

  // Filter state bound to modal controls
  filter: ProductFilter = {
    sortOrder: 'lowToHigh',
    categories: [],
    minRating: undefined
  };
  // For the modal form bindings (kept separate so Cancel discards)
  sortOrderModel: 'lowToHigh' | 'highToLow' = 'lowToHigh';
  selectedCategoriesModel: string[] = [];
  minRatingModel: number | null = null;

  readonly allCategories = ['Home-Decor', 'Electronics', 'LifeStyle', 'Clothing'];

 
  private userSubscription?: Subscription;
 
  // Toast notification properties
  showToast: boolean = false;
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'success';
 
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private userStateService: UserStateService
  ) {}
 
  ngOnInit(): void {
    // THEN: Subscribe to user state for updates
    this.userSubscription = this.userStateService.user$.subscribe(user => {
      if (user) {
        this.currentUserId = user.userId || 0;
       
        // Set cartId from user's cart
        if (user.cart) {
          this.currentCart = user.cart;
          localStorage.setItem('cartId', this.currentCart.cartId.toString());
        }
      }
      this.loadProducts();
    });
 
    // Load all products
  }
 
  ngOnDestroy(): void {
    // Cleanup subscription to prevent memory leaks
    this.userSubscription?.unsubscribe();
  }
 
 
 
  loadProducts(): void {
    this.isLoading = true;
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.filteredProducts = data;
        this.applyFilter();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.isLoading = false;
        this.displayToast('Failed to load products', 'error');
      }
    });
  }
 
  
applyFilter(): void {
  let list = [...this.products];

  if (this.filter.categories && this.filter.categories.length > 0) {
    list = list.filter(p => p.category && this.filter.categories!.includes(p.category));
  }

  if (this.filter.minRating) {
    list = list.filter(p => (p.rating ?? 0) >= this.filter.minRating!);
  }

  list.sort((a, b) => {
    const ap = a.price ?? 0;
    const bp = b.price ?? 0;
    return this.filter.sortOrder === 'highToLow' ? bp - ap : ap - bp;
  });

  this.filteredProducts = list;
  this.resetPagination();
}

applyFilterFromModal(): void {
  // Commit modal models to active filter
  this.filter = {
    sortOrder: this.sortOrderModel,
    categories: [...this.selectedCategoriesModel],
    minRating: this.minRatingModel || undefined
  };
  this.applyFilter();
  this.closeFilter();
}

openFilter(): void {
  // seed modal models from current filter
  this.sortOrderModel = this.filter.sortOrder ?? 'lowToHigh';
  this.selectedCategoriesModel = [...(this.filter.categories ?? [])];
  this.minRatingModel = this.filter.minRating ?? null;
  this.showFilterModal = true;
}

closeFilter(): void {
  this.showFilterModal = false;
}

toggleCategory(cat: string): void {
  const idx = this.selectedCategoriesModel.indexOf(cat);
  if (idx >= 0) {
    this.selectedCategoriesModel.splice(idx, 1);
  } else {
    this.selectedCategoriesModel.push(cat);
  }
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
        this.userStateService.refreshUser();
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        this.displayToast('Failed to add product to cart', 'error');
 
      }
    });
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
 
  closeToast(): void {
    this.showToast = false;
  }
 
  isProductInCart(productId: number): boolean {
    
    return this.currentCart.items?.some(item => item.product.productId === productId) ?? false;
  }

  resetPagination(): void {
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredProducts.length / this.itemsPerPage);
    this.pageNumbers = this.generatePageNumbers();
    this.setPage(this.currentPage);
  }

  setPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;

    const start = (page - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedProducts = this.filteredProducts.slice(start, end);
  }

  onItemsPerPageChange(): void {
    this.resetPagination();
  }

  generatePageNumbers(): number[] {
    const maxVisible = 5;
    let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible - 1);
    if (end - start + 1 < maxVisible) {
      start = Math.max(1, end - maxVisible + 1);
    }
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }

  // Computed
  get from(): number {
    return this.filteredProducts.length === 0 ? 0 : (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  get to(): number {
    return Math.min(this.currentPage * this.itemsPerPage, this.filteredProducts.length);
  }

  get activeFilterCount(): number {
    let count = 0;
    if (this.filter.categories?.length) count++;
    if (this.filter.minRating !== undefined && this.filter.minRating !== null) count++;
    if (this.filter.sortOrder && this.filter.sortOrder !== 'lowToHigh') count++;
    return count;
  }

  clearFilters(): void {
    this.filter = { sortOrder: 'lowToHigh', categories: [], minRating: undefined };
    this.sortOrderModel = 'lowToHigh';
    this.selectedCategoriesModel = [];
    this.minRatingModel = null;
    this.applyFilter();
  }
}
 