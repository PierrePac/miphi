import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSandboxComponent } from './list-sandbox.component';

describe('ListSandboxComponent', () => {
  let component: ListSandboxComponent;
  let fixture: ComponentFixture<ListSandboxComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListSandboxComponent]
    });
    fixture = TestBed.createComponent(ListSandboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
