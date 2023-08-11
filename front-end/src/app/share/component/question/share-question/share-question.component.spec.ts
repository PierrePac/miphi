import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareQuestionComponent } from './share-question.component';

describe('ShareQuestionComponent', () => {
  let component: ShareQuestionComponent;
  let fixture: ComponentFixture<ShareQuestionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShareQuestionComponent]
    });
    fixture = TestBed.createComponent(ShareQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
