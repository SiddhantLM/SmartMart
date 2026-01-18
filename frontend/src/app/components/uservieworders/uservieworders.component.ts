import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Order } from 'src/app/models/order.model';
import { User } from 'src/app/models/user.model';
import { OrderService } from 'src/app/services/order.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-uservieworders',
  templateUrl: './uservieworders.component.html',
  styleUrls: ['./uservieworders.component.css']
})
export class UserviewordersComponent {
  orders: Order[] = [];
  user:User = {}
  loading = true;

  constructor(private orderService: OrderService, private userState:UserStateService, private toastrService:ToastrService) {}

  ngOnInit(): void {
    this.userState.user$.subscribe({
      next:data => {
        this.user = data;
        // this.loadOrders();
        this.orders = data.orders;
        this.loading = false;
      },
      error:(err) => this.toastrService.error("Error fetching user")
    })
  }

  // loadOrders(): void {
  //   this.orderService.getUserOrders(this.user.userId).subscribe({
  //     next: (data) => {
  //       this.orders = data;
  //       this.loading = false;
  //     },
  //     error: () => (this.loading = false)
  //   });
  // }

  getStatusColor(status: string = ''): string {
    switch (status.toLowerCase()) {
      case 'delivered': return '#228B22';
      case 'shipped': return '#1E90FF';
      case 'processing': return '#FFA500';
      case 'cancelled': return '#DC143C';
      default: return '#8B4513';
    }
  }
}
