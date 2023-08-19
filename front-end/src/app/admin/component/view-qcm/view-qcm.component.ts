import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable, Subscription, combineLatest } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';

@Component({
  selector: 'app-view-qcm',
  templateUrl: './view-qcm.component.html',
  styleUrls: ['./view-qcm.component.scss']
})
export class ViewQcmComponent implements OnInit, OnDestroy {
  @Input() qcms$!: Observable<QcmDto[]>;
  allQuestions$: Observable<QuestionDto[]>;
  qcms: QcmDto[] = [];
  //selectedQcm: string | null = null;
  sidebarVisible: boolean = true;
  selectedQcm!: QcmDto;
  selectedQcmQuestions!: QuestionDto[];

  private subscription: Subscription = new Subscription();

  constructor(private questionService: QuestionService,) {
    this.allQuestions$ = this.questionService.questions$;
  }

  ngOnInit(): void {
    this.subscription = this.qcms$.subscribe(data => {
      this.qcms = data;
    });
    this.questionService.loadAllQuestions().subscribe();
    console.log(this.allQuestions$)
  }

  // selectQcm(qcm: string) {
  //   this.selectedQcm = qcm;
  // }

  loadQuestionsForQcm(qcm: any) {
    this.selectedQcm = qcm;
    this.loadQuestions();
  }

  loadQuestions() {
    combineLatest([this.allQuestions$, this.qcms$])
    .pipe(
      map(([questions, qcms]) => {
        const currentQcm = qcms.find(qcm => qcm.id === this.selectedQcm.id);
        if (!currentQcm || !currentQcm.questions) return [];

        const filteredQuestions = currentQcm.questions
          .map(q => questions.find(ques => ques.id === q.id))
          .filter(Boolean) as QuestionDto[];

          filteredQuestions.sort((a, b) => {
            if(a.technologie !== b.technologie && a.technologie && b.technologie) {
              return a.technologie.localeCompare(b.technologie);
            } 
            if  (a.niveau !== b.niveau && a.niveau && b.niveau) {
              return a.niveau.localeCompare(b.niveau);
            } 
            if (a.categorie && b.categorie) {
              return a.categorie.localeCompare(b.categorie);
            }
            return 0;
          })

        return filteredQuestions;
        })
    ).subscribe(questions => {
      this.selectedQcmQuestions = questions || [];
      console.log(this.selectedQcmQuestions);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
