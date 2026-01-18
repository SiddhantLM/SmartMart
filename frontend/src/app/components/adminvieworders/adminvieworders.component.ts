import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/models/order.model';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-adminvieworders',
  templateUrl: './adminvieworders.component.html',
  styleUrls: ['./adminvieworders.component.css'],
})
export class AdminViewOrdersComponent implements OnInit {
  orders: Order[] = [];
  paginatedOrders: Order[] = [];

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;
  pageNumbers: number[] = [];

  // Status options
  statusOptions: string[] = [
    'PENDING',
    'ACCEPTED',
    'PACKED',
    'SHIPPED',
    'DELIVERED',
    'OUTOFSTOCK',
    'OUTFORDELIVERY',
    'CANCELLED',
  ];

  // Computed
  get from(): number {
    return this.orders.length === 0
      ? 0
      : (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  get to(): number {
    return Math.min(this.currentPage * this.itemsPerPage, this.orders.length);
  }

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.updatePagination();
      },
      error: (err) => console.error('Error fetching orders:', err),
    });
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.orders.length / this.itemsPerPage);
    this.pageNumbers = this.generatePageNumbers();
    this.setPage(this.currentPage);
  }

  setPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;

    this.currentPage = page;

    const start = (page - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedOrders = this.orders.slice(start, end);

    // 🔥 Recalculate visible page buttons
    this.pageNumbers = this.generatePageNumbers();
  }

  onItemsPerPageChange(): void {
    this.currentPage = 1;
    this.updatePagination();
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

  updateStatus(orderId: number, newStatus: string): void {
    this.orderService.updateOrderStatus(orderId, newStatus).subscribe({
      next: () => {
        alert('Order status updated successfully!');
        // No need to reload full list — just update locally
        // const order = this.orders.find(o => o.orderId === orderId);
        // if (order) order.status = newStatus;
      },
      error: (err) => {
        console.error('Error updating status:', err);
        alert('Failed to update status');
      },
    });
  }
}
