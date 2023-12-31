import { Component, OnInit } from '@angular/core';
import { Observable, catchError, forkJoin, map, of, switchMap, throwError } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { PropositionDto } from 'src/app/share/dtos/proposition/proposition-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { Categorie } from 'src/app/share/enums/categorie.enum';
import { Niveau } from 'src/app/share/enums/niveau.enum';
import { Technologie } from 'src/app/share/enums/technologie.enum';
import { MessageService } from 'primeng/api';
import { take } from 'rxjs/operators';


@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss'],
  providers: [MessageService]
})
export class QuestionComponent implements OnInit {
  allQuestions$: Observable<QuestionDto[]>;
  filteredQuestions$!: Observable<QuestionDto[]>;
  private originalQuestionsCache: QuestionDto[] = [];
  categories = Object.values(Categorie).map(cat => ({ name: this.formatEnumValue(cat) }));
  technologies = Object.values(Technologie).map(tech => ({ name: tech }));
  niveaux = Object.values(Niveau).map(niv => ({ name: this.formatEnumValue(niv) }));
  toggleAddQuestion: boolean = false;
  first: number = 0;
  rows: number = 10;
  currentQuestionsPage$!: Observable<QuestionDto[]>;

  formatEnumValue(value: string): string {
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  categorieSelectionnee: any;
  technologieSelectionnee: any;
  niveauSelectionne: any;

  constructor(private questionService: QuestionService,
              private reponseService: ReponseService,
              private messageService: MessageService)
              {
    this.allQuestions$ = this.questionService.questions$;
    this.filteredQuestions$ = this.allQuestions$;
  }

  ngOnInit():void {
    this.questionService.loadAllQuestions().subscribe();
    this.updateCurrentPageQuestions();
  }

  show(message: string, type: string) {
    if(type === 'error')
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: message });
    if(type === 'warning')
    this.messageService.add({ severity: 'warn', summary: 'warn', detail: message });
    if(type === 'success')
    this.messageService.add({ severity: 'success', summary: 'success', detail: message });
  }

  onSubmit() {
    this.questionService.loadAllQuestions();
    this.updateCurrentPageQuestions();
  }

  handleSave(questionValue: QuestionDto) {
    const question: QuestionDto = {
      id: questionValue.id,
      temps: questionValue.temps,
      point: questionValue.point,
      question: questionValue.question,
      categorie: questionValue.categorie,
      technologie: questionValue.technologie,
      niveau: questionValue.niveau
    };
    const reponses: PropositionDto[] = questionValue.reponses?.map((reponse: PropositionDto) => ({
        id: reponse.id,
        reponse: reponse.reponse,
        correct: reponse.correct,
        question_id: questionValue.id
    })) ?? [];

    if(!questionValue.id) {
      this.questionService.addQuestion(question).pipe(
        switchMap((reponse) => {
          const newquestionId = reponse.id;
          const reponseObservables = questionValue.reponses?.map(reponse => {
            if(reponse.reponse) {
              const reponseDto: PropositionDto = {
                reponse: reponse.reponse,
                correct: reponse.correct,
                question_id: newquestionId
              };
              this.reponseService.addReponse(reponseDto).subscribe();
            }
            return of(null);
          }) ?? [];
          return forkJoin(reponseObservables);
        }),
        switchMap(() => this.questionService.loadAllQuestions())
        ).subscribe(questions => {
          this.show('Question Ajoutée avec succès', 'success');
          this.questionService.updateQuestions(questions);
        }, error => {
          this.show('Une erreur est survenu lors de l\'ajout de la question', 'error')
        });
    } else {
      this.updateLocalQuestionsCache(questionValue, reponses);
      
      this.questionService.modifyQuestion(questionValue.id, question).pipe(
        switchMap(() => {
          const reponseObservables = reponses.map(reponse => {
            if(reponse.id && reponse.reponse) {
              this.reponseService.modifyReponse(reponse.id, reponse).subscribe();
            } else if (reponse.reponse) {
              this.reponseService.addReponse(reponse).subscribe();
            }
            return of(null);
          });
          this.show('Question modifiée avec succès', 'success');
          console.log()
          return forkJoin(reponseObservables);
        }),
        catchError((error) => {
          this.show('Une erreur est survenue lors de la modification de la question', 'error');
          this.revertLocalQuestionsCache();
          return throwError(error);
        })
      ).subscribe();
    }
  }
  
  private updateLocalQuestionsCache(question: QuestionDto, reponses: PropositionDto[]) {

    this.allQuestions$.subscribe(questions => this.originalQuestionsCache = [...questions]);

    this.allQuestions$ = this.allQuestions$.pipe(
      map(questions => {
        const index = questions.findIndex(q => q.id === question.id);
        question.reponses = reponses;
        if (index > -1) {
          questions[index] = question;
          questions[index].reponses = reponses
        } else {
          questions.push(question);
        }
        return questions;
      })
    );
    this.allQuestions$.subscribe(questions => {
      localStorage.setItem('questions_cache', JSON.stringify(questions));
    });
    this.updateQuestionsList();
  }



  private revertLocalQuestionsCache() {
    this.allQuestions$ = of(this.originalQuestionsCache);
    localStorage.setItem('questions_cache', JSON.stringify(this.originalQuestionsCache));
  }

  updateQuestionsList() {
    this.filteredQuestions$ = this.allQuestions$.pipe(
      map(questions => questions.filter(question =>
        (!this.categorieSelectionnee || question.categorie === this.categorieSelectionnee.name.toUpperCase()) &&
        (!this.technologieSelectionnee || question.technologie === this.technologieSelectionnee.name.toUpperCase()) &&
        (!this.niveauSelectionne || question.niveau === this.niveauSelectionne.name.toUpperCase())
      ))
    );
    this.updateCurrentPageQuestions();
  }

  deleteQuestion(question: QuestionDto) {
    if(question.id) {
      this.questionService.deleteQuestion(question.id).subscribe(() => {
        this.allQuestions$ = this.allQuestions$.pipe(
          map(questions => questions.filter(q => q.id !== question.id))
        );
        this.allQuestions$.subscribe(questions => {
          localStorage.setItem('questions_cache', JSON.stringify(questions));
        })
      })
      this.show('Question supprimé avec succès', 'success');
    }
  }

  reinitialiserFiltres() {
    this.categorieSelectionnee = null;
    this.technologieSelectionnee = null;
    this.niveauSelectionne = null;
    this.updateQuestionsList();
  }

  updateCurrentPageQuestions(): void {
    this.currentQuestionsPage$ = this.filteredQuestions$.pipe(
      map(questions => questions.slice(this.first, this.first + this.rows))
    );
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.updateCurrentPageQuestions();
  }

}
