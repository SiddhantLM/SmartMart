import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewcartComponent } from './userviewcart.component';

describe('UserviewcartComponent', () => {
  let component: UserviewcartComponent;
  let fixture: ComponentFixture<UserviewcartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserviewcartComponent]
    });
    fixture = TestBed.createComponent(UserviewcartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
