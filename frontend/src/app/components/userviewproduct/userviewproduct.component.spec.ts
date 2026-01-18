import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewproductComponent } from './userviewproduct.component';

describe('UserviewproductComponent', () => {
  let component: UserviewproductComponent;
  let fixture: ComponentFixture<UserviewproductComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserviewproductComponent]
    });
    fixture = TestBed.createComponent(UserviewproductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
