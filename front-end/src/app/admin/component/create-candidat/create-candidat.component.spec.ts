import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCandidatComponent } from './create-candidat.component';

describe('CreateCandidatComponent', () => {
  let component: CreateCandidatComponent;
  let fixture: ComponentFixture<CreateCandidatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateCandidatComponent]
    });
    fixture = TestBed.createComponent(CreateCandidatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
