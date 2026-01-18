import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAddCartComponent } from './useraddcart.component';

describe('UseraddcartComponent', () => {
  let component: UserAddCartComponent;
  let fixture: ComponentFixture<UserAddCartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserAddCartComponent]
    });
    fixture = TestBed.createComponent(UserAddCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
