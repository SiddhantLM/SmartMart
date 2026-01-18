import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-adminviewproduct',
  templateUrl: './adminviewproduct.component.html',
  styleUrls: ['./adminviewproduct.component.css']
})
export class AdminviewproductComponent implements OnInit {

  products: Product[] = [];
  filteredProducts: Product[] = [];
  paginatedProducts: Product[] = [];

  categories: string[] = [];
  selectedCategory: string = '';

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;
  pageNumbers: number[] = [];

  // Modal
  showEditPopup: boolean = false;
  editProductData: Product | null = null;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe((data: Product[]) => {
      this.products = data;
      this.extractCategories();
      this.applyFilters();
    });
  }

  extractCategories(): void {
    const cats = this.products
      .map(p => p.category)
      .filter(Boolean);
    this.categories = Array.from(new Set(cats)).sort();
  }

  applyFilters(): void {
    if (this.selectedCategory) {
      this.filteredProducts = this.products.filter(p =>
        p.category === this.selectedCategory
      );
    } else {
      this.filteredProducts = [...this.products];
    }
    this.resetPagination();
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

  // Computed getters for pagination info
  get from(): number {
    return this.filteredProducts.length === 0 ? 0 : (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  get to(): number {
    return Math.min(this.currentPage * this.itemsPerPage, this.filteredProducts.length);
  }

  // Actions
  openEditPopup(product: Product): void {
    this.editProductData = { ...product };
    this.showEditPopup = true;
  }

  closeEditPopup(): void {
    this.showEditPopup = false;
    this.editProductData = null;
  }

  updateProduct(): void {
    if (this.editProductData && this.editProductData.productId) {
      this.productService.updateProduct(this.editProductData.productId, this.editProductData)
        .subscribe({
          next: () => {
            this.loadProducts();
            this.closeEditPopup();
            alert('Product updated successfully!');
          },
          error: () => alert('Failed to update product')
        });
    }
  }

  deleteProduct(productId: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          this.loadProducts();
          alert('Product deleted successfully');
        },
        error: () => alert('Failed to delete product')
      });
    }
  }

  handleImageError(event: any): void {
    event.target.src = 'assets/images/no-image.png';
  }
}