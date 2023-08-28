import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionQcmDto } from 'src/app/share/dtos/question/question-qcm-dto';


@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrls: ['./candidat.component.scss']
})
export class CandidatComponent implements OnInit, OnDestroy {
  questionsList!: QuestionDto[];
  filteredQuestion!: QuestionDto[];
  questionQcm!: QuestionQcmDto[];


  constructor(private router: Router,
              private questionService: QuestionService,
              private qcmService: QcmService) { }

  ngOnInit(): void {
    const personneJSON = sessionStorage.getItem('personne');
    const personne = personneJSON ? JSON.parse(personneJSON) : null;
    this.questionService.loadAllQuestionsWr().subscribe(
      (data: QuestionDto[]) => {
        this.questionsList = data;
        this.qcmService.getQcmQuestionByEntretien(personne.entretienId).subscribe(
          (data: QuestionQcmDto[]) => {
            this.questionQcm = data;
            this.loadQuestions()
          }
        )
      }
    )
  }

  loadQuestions() {
    of(this.questionsList).pipe(
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
      this.filteredQuestion = sortedAndFilteredQuestions;
      sessionStorage.setItem('question_qcm', JSON.stringify(this.filteredQuestion));
    });
  }

  towardTest(){
    this.router.navigate(['/entretien/qcm']);
  }

  ngOnDestroy(): void {
  }
}
