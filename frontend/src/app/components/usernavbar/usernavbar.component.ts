import { Component, Input, SimpleChanges } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-usernavbar',
  templateUrl: './usernavbar.component.html',
  styleUrls: ['./usernavbar.component.css'],
})
export class UsernavbarComponent {
  @Input() user: User = {};
  cartCount: number = 0;

  constructor(
    private userState: UserStateService,
    private authService: AuthService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    // Update cart count whenever user input changes
    if (changes['user'] && this.user?.cart?.items) {
      this.cartCount = this.user.cart.items.length;
    }
  }

  ngOnInit() {
    // const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    this.cartCount = this.user.cart.items.length;
  }
  logout(): void {
    this.authService.logout();
    this.userState.userId = null;
    window.location.href = '/login';
  }
}
