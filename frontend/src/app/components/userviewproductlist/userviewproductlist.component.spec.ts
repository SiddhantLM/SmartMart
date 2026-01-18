import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewproductlistComponent } from './userviewproductlist.component';

describe('UserviewproductlistComponent', () => {
  let component: UserviewproductlistComponent;
  let fixture: ComponentFixture<UserviewproductlistComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserviewproductlistComponent]
    });
    fixture = TestBed.createComponent(UserviewproductlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
