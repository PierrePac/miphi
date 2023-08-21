import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable, Subscription, combineLatest } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionQcmDto } from 'src/app/share/dtos/question/question-qcm-dto';
import { QcmQuestionDto } from 'src/app/share/dtos/qcm/qcm-question-dto';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';

@Component({
  selector: 'app-view-qcm',
  templateUrl: './view-qcm.component.html',
  styleUrls: ['./view-qcm.component.scss']
})
export class ViewQcmComponent implements OnInit, OnDestroy {
  @Input() qcms$!: Observable<QcmDto[]>;
  allQuestions$: Observable<QuestionDto[]>;
  qcms: QcmDto[] = [];
  sidebarVisible: boolean = true;
  selectedQcm!: QcmDto;
  selectedQcmQuestions!: QuestionDto[];
  questionsWithOrder: QuestionQcmDto[] = [];


  private subscription: Subscription = new Subscription();

  constructor(private questionService: QuestionService,
              private qcmService: QcmService) {
    this.allQuestions$ = this.questionService.questions$;
  }

  ngOnInit(): void {
    this.subscription = this.qcms$.subscribe(data => {
      this.qcms = data;
    });
    this.questionService.loadAllQuestions().subscribe();
  }

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
          const questionsWithOrder: QuestionQcmDto[] = (currentQcm.questions as unknown as QcmQuestionDto[])
              .map(q => {
                const matchingQuestion = questions.find(ques => ques.id === q.questionIds);
                if (matchingQuestion) {
                  return {
                    id: q.id,
                    ordre: q.ordre,
                    question: matchingQuestion
                  };
                }
                return null;
              })
            .filter(Boolean) as QuestionQcmDto[];
              questionsWithOrder.sort((a, b) => a.ordre - b.ordre);
              console.group(questionsWithOrder)
            return questionsWithOrder;
          })
        )
        .subscribe(questionsWithOrder => {
            this.questionsWithOrder = questionsWithOrder;
        });
  }

  onQuestionsReordered() {
    this.questionsWithOrder.forEach((question, index) => {
      question.ordre = index;
    });

    console.log(this.questionsWithOrder);

    this.qcmService.updateQuestionsOrder(this.questionsWithOrder).subscribe(resp => {
      console.log('ordres de questions mise à jour');
    }, error => {
      console.error('Erreur dans la mise à jour de l\'ordre des questions')
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
