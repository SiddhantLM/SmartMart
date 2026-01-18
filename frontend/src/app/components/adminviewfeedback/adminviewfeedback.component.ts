import { Component, OnInit } from '@angular/core';
import { Feedback } from 'src/app/models/feedback.model';
import { FeedbackService } from 'src/app/services/feedback.service';

@Component({
  selector: 'app-adminviewfeedback',
  templateUrl: './adminviewfeedback.component.html',
  styleUrls: ['./adminviewfeedback.component.css'],
})
export class AdminviewfeedbackComponent implements OnInit {
  feedBackList: Feedback[] = [];
  paginatedFeedback: Feedback[] = [];

  // Pagination properties
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;

  selectedUser: {
    email: string;
    username: string;
    mobileNumber: string;
  } | null = null;
  showPopup: boolean = false;

  constructor(private service: FeedbackService) {}

  ngOnInit(): void {
    this.service.getAllFeedback().subscribe((data) => {
      console.log('Raw feedback data:', data);
      this.feedBackList = data;
      this.updatePagination();
    });
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.feedBackList.length / this.itemsPerPage);
    this.setPage(this.currentPage); // Refresh current page
  }

  setPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;

    const start = (page - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedFeedback = this.feedBackList.slice(start, end);
  }

  onItemsPerPageChange(): void {
    this.currentPage = 1;
    this.updatePagination();
  }

  get pageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisible = 5;

    let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible - 1);

    if (end - start + 1 < maxVisible) {
      start = Math.max(1, end - maxVisible + 1);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  openUserDetails(feedback: Feedback) {
    this.selectedUser = {
      email: feedback.user.email || 'N/A',
      username: feedback.user.username,
      mobileNumber: feedback.user.mobileNumber || 'N/A',
    };
    this.showPopup = true;
  }

  closePopup() {
    this.showPopup = false;
    this.selectedUser = null;
  }

  get from(): number {
    return this.feedBackList.length === 0
      ? 0
      : (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  get to(): number {
    return Math.min(
      this.currentPage * this.itemsPerPage,
      this.feedBackList.length
    );
  }
}
