import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewQcmComponent } from './view-qcm.component';

describe('ViewQcmComponent', () => {
  let component: ViewQcmComponent;
  let fixture: ComponentFixture<ViewQcmComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewQcmComponent]
    });
    fixture = TestBed.createComponent(ViewQcmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
