import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewwishlistComponent } from './userviewwishlist.component';

describe('UserviewwishlistComponent', () => {
  let component: UserviewwishlistComponent;
  let fixture: ComponentFixture<UserviewwishlistComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserviewwishlistComponent]
    });
    fixture = TestBed.createComponent(UserviewwishlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
