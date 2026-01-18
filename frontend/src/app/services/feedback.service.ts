import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Feedback } from '../models/feedback.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  private apiUrl = `${environment.apiUrl}/feedback`;

  constructor(private http: HttpClient) { }

  createFeedback(userId:Number,feedback: Feedback): Observable<Feedback> {
    return this.http.post<Feedback>(this.apiUrl+"/user/"+userId, feedback);
  }

  getFeedbackByUserId(userId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/user/${userId}`);
  }

  getFeedbackById(feedbackId: number): Observable<Feedback> {
    return this.http.get<Feedback>(`${this.apiUrl}/${feedbackId}`);
  }

  deleteFeedback(feedbackId: number): Observable<Feedback> {
    return this.http.delete<Feedback>(`${this.apiUrl}/${feedbackId}`);
  }

  getAllFeedback(): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(this.apiUrl);
  }
  getUserById(userId: number): Observable<User>{
    return this.http.get<User>(this.apiUrl+"/"+userId) ///newMethod
  }

 
}

