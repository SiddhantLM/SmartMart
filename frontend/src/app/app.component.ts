import { Component } from '@angular/core';
import { User } from './models/user.model';
import { Subscription } from 'rxjs';
import { UserStateService } from './services/user-state.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'angularapp';
  user: User | null = null;
  private userSubscription?: Subscription;
  isAdmin: boolean = false;
  isUser: boolean = false;

  constructor(private userStateService: UserStateService) {}

  ngOnInit(): void {
    // Subscribe to user changes
    this.userSubscription = this.userStateService.user$.subscribe((user) => {
      if (user) {
        this.user = user;
        if (user.userRole === 'ADMIN') {
          this.isAdmin = true;
          this.isUser = false;
        } else if (user.userRole === 'USER') {
          this.isUser = true;
          this.isAdmin = false;
        } else {
          this.isAdmin = false;
          this.isUser = false;
        }
      } else {
        this.isAdmin = false;
        this.isUser = false;
        this.user = null;
      }
    });
  }

  ngOnDestroy(): void {
    // Clean up subscription to prevent memory leaks
    this.userSubscription?.unsubscribe();
  }
}
