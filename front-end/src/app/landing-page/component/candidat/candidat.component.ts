import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';


@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrls: ['./candidat.component.scss']
})
export class CandidatComponent implements OnInit, OnDestroy {
  private entretienSubscription?: Subscription;
  private entretien?: EntretienDto;
  private questionSubscription?: Subscription;
  private question?: QuestionDto[];


  constructor(private router: Router,
              private entretienService: EntretienService,
              private questionService: QuestionService) { }

  ngOnInit(): void {
    this.questionService.loadAllQuestions().subscribe();
    this.entretienSubscription = this.entretienService.entretien$.subscribe((data) => {
      if(data) {
        console.log('entretien: ', data)
        this.entretien = data;
      }
    },
    (error) => {
      console.error('Echec de chargement de l\'entretien', error);
    });

    this.questionSubscription = this.questionService.questions$.subscribe((data) => {
      if(data) {
        console.log('question: ', data)
        this.question = data;
      }
    },
    (error) => {
      console.error('Echec de chargement des questions', error);
    })


  }

  towardTest(){
    this.router.navigate(['/entretien/qcm']);
  }

  ngOnDestroy(): void {
    if (this.entretienSubscription) {
      this.entretienSubscription.unsubscribe();
    }
  }
}
