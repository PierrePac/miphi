import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { FullEntretienDto } from 'src/app/share/dtos/entretien/full-entretien-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';


@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrls: ['./candidat.component.scss']
})
export class CandidatComponent implements OnInit, OnDestroy {
  private entretienSubscription?: Subscription;
  private entretien?: FullEntretienDto;
  private questionSubscription?: Subscription;
  private question?: QuestionDto[];
  private combinedSubscription?: Subscription;


  constructor(private router: Router,
              private entretienService: EntretienService,
              private questionService: QuestionService) { }

  ngOnInit(): void {
    this.questionService.loadAllQuestionsWr().subscribe();
    this.entretienSubscription = this.entretienService.entretien$.subscribe((data) => {
      if(data) {
        console.log(data)
        this.entretien = data;
      }
    },
    (error) => {
      console.error('Echec de chargement de l\'entretien', error);
    });

    this.questionSubscription = this.questionService.questionsWr$.subscribe((data) => {
      if(data) {
        //console.log(data)
        this.question = data;
      }
    },
    (error) => {
      console.error('Echec de chargement des questions', error);
    });

    this.combinedSubscription = combineLatest([this.entretienService.entretien$, this.questionService.questionsWr$])
      .pipe(
        map(([entretien, allQuestions]) => {
          if (!entretien || !allQuestions) {
            return null;
          }
          const questionsIds = entretien.qcm.questions.map((q: { id: number }) => q.id);

          const filteredQuestions = allQuestions.filter(question =>
            question.id !== undefined && questionsIds.includes(question.id)
          );

          return { entretien, filteredQuestions };
        })
      ).subscribe((combinedData) => {
        if (combinedData) {
          sessionStorage.setItem('entretien', JSON.stringify(combinedData));
        }
      },
      (error) => {
        console.error('Erreur dans combineLatest', error);
      })
  }

  towardTest(){
    this.router.navigate(['/entretien/qcm']);
  }

  ngOnDestroy(): void {
    if (this.entretienSubscription) {
      this.entretienSubscription.unsubscribe();
    }
    if(this.questionSubscription) {
      this.questionSubscription.unsubscribe();
    }
    if (this.combinedSubscription) {
      this.combinedSubscription.unsubscribe();
    }
  }
}
