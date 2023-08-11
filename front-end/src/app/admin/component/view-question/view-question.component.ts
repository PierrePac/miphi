import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { ReponseDto } from 'src/app/share/dtos/reponse/reponse-dto';


@Component({
  selector: 'app-view-question',
  templateUrl: './view-question.component.html',
  styleUrls: ['./view-question.component.scss']
})
export class ViewQuestionComponent implements OnInit {
  question$: Observable<QuestionDto[]> = this.questionService.question$;

  constructor(private questionService: QuestionService,
              private reponseService: ReponseService ) {}

  ngOnInit():void {
    this.questionService.getAllQuestions().subscribe(questions => {
      this.question$ = of(questions);
  });
  }

  onSubmit() {
        this.questionService.getAllQuestions();
  }

  handleSave(questionValue: QuestionDto) {
    if(!questionValue.id) {
      const newQuestion: QuestionDto = {
        temps: questionValue.temps,
        point: questionValue.point,
        question: questionValue.question,
        categorie: questionValue.categorie,
        technologie: questionValue.technologie,
        niveau: questionValue.niveau
      }
      this.questionService.addQuestion(newQuestion).subscribe(reponse => {
        const newquestionId = reponse.id;
        const newquestion = reponse;
        questionValue.reponses?.forEach(reponse => {
          if(reponse.reponse) {
            const reponseDto: ReponseDto = {
              reponse: reponse.reponse,
              correct: reponse.correct,
              question_id: newquestionId
            };
            this.reponseService.addReponse(reponseDto).subscribe();
          }
        });
        this.updateQuestions(newquestion);
      });
    } else {
      this.questionService.modifyQuestion(questionValue.id, questionValue).subscribe(() => {
        this.updateQuestions(questionValue);
      })
    }
  }

  updateQuestions(newOrUpdatedQuestion: QuestionDto) {
    this.questionService.getAllQuestions().subscribe(questions => {
      this.question$ = of(questions);
    });
  }
}
