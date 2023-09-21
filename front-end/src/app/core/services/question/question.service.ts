import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class QuestionService {
  // BehaviorSubjects pour garder la trace des questions
  private questionSubject = new BehaviorSubject<QuestionDto[]>([]);
  private questionSubjectWr = new BehaviorSubject<QuestionDto[]>([]);

  // Observables qui écoutent les BehaviorSubjects
  questions$ = this.questionSubject.asObservable().pipe(shareReplay(1));
  questionsWr$ = this.questionSubjectWr.asObservable().pipe(shareReplay(1));

  constructor(private httpClient: HttpClient) { }

  // Charger toutes les questions et les stocker dans questionSubject
  loadAllQuestions(): Observable<QuestionDto[]> {
    return this.getAllQuestions().pipe(
      tap(questions => {
      this.questionSubject.next(questions);
      this.saveQuestionsToCache(questions);
      })
    );
  }

  // Charger toutes les questions sans les réponses (toutes les réponses sont false) et les stocker dans questionSubjectWr
  loadAllQuestionsWr(): Observable<QuestionDto[]> {
    return this.getAllQuestionsWr().pipe(
      tap(questions => {
      this.questionSubjectWr.next(questions);
      this.saveQuestionsToCache(questions);
      })
    );
  }

  // Sauvegarder les questions dans le stockage local
  private saveQuestionsToCache(questions: QuestionDto[]) {
    localStorage.setItem('questions_cache', JSON.stringify(questions));
  }

  // Mettre à jour questionSubject avec un nouvel ensemble de questions
  setQuestions(questions: QuestionDto[]) {
    this.questionSubject.next(questions);
  }

  // Récupérer toutes les questions et les trier par id
  getAllQuestions(): Observable<QuestionDto[]> {
    const sortQuestions = (questions: QuestionDto[]) =>
      questions.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));

    return this.httpClient.get<QuestionDto[]>(environment.GetAllQuestionUrl).pipe(
      map(questions => sortQuestions(questions)),
      map(questions => {
        this.questionSubject.next(questions);
        return questions;
      })
    );
  }

  // Récupérer toutes les questions sans les réponses (toutes les réponses sont false) et les trier par id
  getAllQuestionsWr(): Observable<QuestionDto[]> {
    const sortQuestions = (questions: QuestionDto[]) =>
      questions.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));

    return this.httpClient.get<QuestionDto[]>(environment.GetAllQuestionUrlWr).pipe(
      map(questions => sortQuestions(questions)),
      map(questions => {
        this.questionSubject.next(questions);
        return questions;
      })
    );
  }

  // Mettre à jour le cache local après une action spécifique (ajout, modification, suppression)
  private updateLocalCache(action: (questions: QuestionDto[]) => void) {
    const currentQuestions = this.questionSubject.value;
    action(currentQuestions);
    this.questionSubject.next(currentQuestions);
    this.saveQuestionsToCache(currentQuestions);
  }

  // Méthodes pour mettre à jour le cache local après une action d'ajout
  updateLocalCacheAfterAdd(newQuestion: QuestionDto) {
    this.updateLocalCache(questions => questions.push(newQuestion));
  }

  // Méthodes pour mettre à jour le cache local après une action de modification
  updateLocalCacheAfterModify(modifiedQuestion: QuestionDto) {
    this.updateLocalCache(questions => {
      const index = questions.findIndex(q => q.id === modifiedQuestion.id);
      if (index > -1) {
        questions[index] = modifiedQuestion;
      }
    });
  }

  // Méthodes pour mettre à jour le cache local après une action de suppression
  updateLocalCacheAfterDelete(id: number) {
    this.updateLocalCache(questions => {
      const index = questions.findIndex(q => q.id === id);
      if (index > -1) {
        questions.splice(index, 1);
      }
    });
  }

  // Méthodes d'ajout de question
  addQuestion(question: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.post<QuestionDto>(environment.addQuestion, question).pipe(
      tap(newQuestion => this.updateLocalCacheAfterAdd(newQuestion))
    );
  }

  // Méthodes de modification de question
  modifyQuestion(id: number, question: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.put<QuestionDto>(`${environment.modifyQuestion}${id}`, question).pipe(
      tap(updatedQuestion => this.updateLocalCacheAfterModify(updatedQuestion))
    );
  }

  // Méthodes de suppression de question
  deleteQuestion(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.deleteQuestion}${id}`).pipe(
      tap(() => this.updateLocalCacheAfterDelete(id))
    );
  }

  // Mettre à jour questionSubject et le cache local
  updateQuestions(questions: QuestionDto[]) {
    this.questionSubject.next(questions);
    localStorage.setItem('questions_cache', JSON.stringify(questions));
  }

  // Obtenir les questions d'un QCM spécifique
  getQuestionsOfQcm(id: number): Observable<QuestionDto[]> {
    return this.httpClient.get<QuestionDto[]>(`${environment.getQuestionFromQcm}${id}`);
  }
}
