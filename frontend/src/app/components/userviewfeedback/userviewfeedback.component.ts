import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Feedback } from 'src/app/models/feedback.model';
import { FeedbackService } from 'src/app/services/feedback.service';
import { UserStateService } from 'src/app/services/user-state.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-userviewfeedback',
  templateUrl: './userviewfeedback.component.html',
  styleUrls: ['./userviewfeedback.component.css']
})
export class UserviewfeedbackComponent implements OnInit{

  myFeedbacks:Feedback[]=[]
  userId:number;

  showDeletedPopup:boolean=false
  selectedFeedBackId:number;

  constructor(private service:FeedbackService ,private route:ActivatedRoute ,private router:Router, private userState:UserStateService){}
  
  ngOnInit(): void {
    this.userState.user$.subscribe(data => {
      this.userId = data.userId;
      this.loadFeedback();
    })
  }

  loadFeedback() {
    this.service.getFeedbackByUserId(this.userId).subscribe(data=>this.myFeedbacks=data)
  }

  deleteFeedback(id:number) {
    this.service.deleteFeedback(id).subscribe()
  }

  openPopup(id:number) {
    this.selectedFeedBackId=id
    this.showDeletedPopup=true
  }

  confirmDelete() {
    this.service.deleteFeedback(this.selectedFeedBackId).subscribe(()=>
    {
      this.showDeletedPopup=false
      this.loadFeedback();
    })
  }

  cancelDelete() {
    this.showDeletedPopup=false
    this.selectedFeedBackId = null;
  }

}
