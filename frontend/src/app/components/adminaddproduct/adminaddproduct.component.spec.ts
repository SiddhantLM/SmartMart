import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminaddproductComponent } from './adminaddproduct.component';

describe('AdminaddproductComponent', () => {
  let component: AdminaddproductComponent;
  let fixture: ComponentFixture<AdminaddproductComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminaddproductComponent]
    });
    fixture = TestBed.createComponent(AdminaddproductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
