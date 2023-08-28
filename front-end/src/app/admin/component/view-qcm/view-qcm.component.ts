import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionQcmDto } from 'src/app/share/dtos/question/question-qcm-dto';
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
  questionQcm!: QuestionQcmDto[]
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
      this.qcmService.getQcmQuestion(qcm.id).subscribe(
        (data: QuestionQcmDto[]) => {
        this.questionQcm = data;
        this.loadQuestions();
      }
    )
  }

  loadQuestions() {
    this.allQuestions$.pipe(
      map(allQuestions => {
        // Filtre les questions
        return allQuestions.filter(q => {
          return this.questionQcm.some(qcm => qcm.idQuestion === q.id);
        });
      }),
      map(filteredQuestions => {
        // Trie les questions filtrées selon leur ordre
        return filteredQuestions.sort((a, b) => {
          const ordreA = this.questionQcm.find(qcm => qcm.idQuestion === a.id)?.ordre;
          const ordreB = this.questionQcm.find(qcm => qcm.idQuestion === b.id)?.ordre;

          if (ordreA === undefined || ordreB === undefined) {
            return 0; // ou autre logique de tri pour les valeurs non définies
          }

          return ordreA - ordreB;
        });
      })
    ).subscribe(sortedAndFilteredQuestions => {
      this.selectedQcmQuestions = sortedAndFilteredQuestions;
      console.log(this.questionQcm)
      console.log(this.selectedQcmQuestions);
    });
  }

   onQuestionsReordered() {
  this.selectedQcmQuestions.forEach((question, index) => {
    const foundQcm = this.questionQcm.find(qcm => qcm.idQuestion === question.id);
    if (foundQcm) {
      foundQcm.ordre = index;
    }
  });
  this.questionQcm.sort((a, b) => a.ordre - b.ordre);
  this.qcmService.updateQuestionsOrder(this.questionQcm)
    .subscribe(response => {
      console.log('Ordre mis à jour:', response);
    }, error => {
      console.error('Erreur lors de la mise à jour de l\'ordre:', error);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
