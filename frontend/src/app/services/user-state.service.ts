import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { BehaviorSubject, tap } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserStateService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();
  apiUrl = environment.apiUrl;

  constructor(private http:HttpClient) {
    const savedId = localStorage.getItem('user_id');
    if (savedId) {
      this.userId = savedId;
    }
  }

  get user(): User | null {
    return this.userSubject.getValue();
  }

  // Setter: When you set a user ID, it fetches full user from API
  set userId(id: string | number | null) {
    if (!id) {
      this.userSubject.next(null);
      return;
    }

    this.http.get<User>(`${this.apiUrl}/user/${id}`).pipe(
      tap(fullUser => {
        this.userSubject.next(fullUser);  // Update state with full user
        console.log('User loaded:', fullUser);
      })
    ).subscribe({
      error: (err) => {
        console.error('Failed to load user', err);
        this.userSubject.next(null);
      }
    });
  }

  refreshUser(): void {
    const currentUser = this.userSubject.getValue();
  
    // If no user is logged in → do nothing
    if (!currentUser?.userId) {
      return;
    }
  
    this.http.get<User>(`${this.apiUrl}/user/${currentUser.userId}`).pipe(
      tap(fullUser => {
        this.userSubject.next(fullUser);   // ← updates every subscriber instantly
        console.log('User refreshed:', fullUser);
      }),
    ).subscribe();
  }
}
