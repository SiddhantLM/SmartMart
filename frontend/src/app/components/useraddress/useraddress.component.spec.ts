import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UseraddressComponent } from './useraddress.component';

describe('UseraddressComponent', () => {
  let component: UseraddressComponent;
  let fixture: ComponentFixture<UseraddressComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UseraddressComponent]
    });
    fixture = TestBed.createComponent(UseraddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
