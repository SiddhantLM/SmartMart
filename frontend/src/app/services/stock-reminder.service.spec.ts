import { TestBed } from '@angular/core/testing';

import { StockReminderService } from './stock-reminder.service';

describe('StockReminderService', () => {
  let service: StockReminderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockReminderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
