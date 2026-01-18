import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FeedbackService } from 'src/app/services/feedback.service';
import { UserStateService } from 'src/app/services/user-state.service';

@Component({
  selector: 'app-useraddfeedback',
  templateUrl: './useraddfeedback.component.html',
  styleUrls: ['./useraddfeedback.component.css']
})
export class UseraddfeedbackComponent implements OnInit {

  feedbackForm:FormGroup;
  userId:number = null;

  constructor(private service:FeedbackService,private userState:UserStateService,private router:Router, private fb:FormBuilder){}
  ngOnInit(): void {
    this.userState.user$.subscribe(data => {
      this.userId = data.userId;
    })
    this.feedbackForm=this.fb.group(
      {
        message:['',Validators.required],
        rating:[null,[Validators.required,Validators.min(1),Validators.max(5)]]
      }
    )
  }

  submit()
  {
    if(this.feedbackForm.valid && this.userId)
    {
      this.service.createFeedback(this.userId,this.feedbackForm.value).subscribe((data)=>
      {
        this.feedbackForm.reset();
        this.userState.refreshUser();        
      })
    }
  }

  
}
