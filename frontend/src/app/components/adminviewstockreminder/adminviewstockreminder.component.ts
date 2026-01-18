import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { StockReminder } from 'src/app/models/stock-reminder.model';
import { StockReminderService } from 'src/app/services/stock-reminder.service';

@Component({
  selector: 'app-adminviewstockreminder',
  templateUrl: './adminviewstockreminder.component.html',
  styleUrls: ['./adminviewstockreminder.component.css'],
})
export class AdminviewstockreminderComponent implements OnInit {
  stockReminder: StockReminder[] = [];

  constructor(
    private stockReminderservice: StockReminderService,
    private toasterservice: ToastrService
  ) {}

  ngOnInit(): void {
    this.stockReminderservice.getReminder().subscribe((d) => {
      this.stockReminder = d;
    });
  }

  sendReminder(reminderId: Number) {
    this.stockReminderservice.sendReminder(reminderId).subscribe({
      next: (data) => {
        console.log(data);
        const reminder = this.stockReminder.find((r) => r.id === reminderId);
        if (reminder) reminder['sent'] = true;
        this.toasterservice.success('Reminder sent!!');
      },
      error: (error) => {
        console.log(error);
        this.toasterservice.error('Failed to send Reminder');
      },
    });
  }
}
