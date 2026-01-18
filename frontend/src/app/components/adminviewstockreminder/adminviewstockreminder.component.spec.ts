import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminviewstockreminderComponent } from './adminviewstockreminder.component';

describe('AdminviewstockreminderComponent', () => {
  let component: AdminviewstockreminderComponent;
  let fixture: ComponentFixture<AdminviewstockreminderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminviewstockreminderComponent]
    });
    fixture = TestBed.createComponent(AdminviewstockreminderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
