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
  private questionSubject = new BehaviorSubject<QuestionDto[]>([]);
  questions$ = this.questionSubject.asObservable().pipe(
    shareReplay(1)
  );

  constructor(private httpClient: HttpClient) { }

  loadAllQuestions(): Observable<QuestionDto[]> {
    return this.getAllQuestions().pipe(
      tap(questions => {
      this.questionSubject.next(questions);
      this.saveQuestionsToCache(questions);
      })
    );
  }

  private saveQuestionsToCache(questions: QuestionDto[]) {
    localStorage.setItem('questions_cache', JSON.stringify(questions));
  }

  setQuestions(questions: QuestionDto[]) {
    this.questionSubject.next(questions);
  }

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

  private updateLocalCache(action: (questions: QuestionDto[]) => void) {
    const currentQuestions = this.questionSubject.value;
    action(currentQuestions);
    this.questionSubject.next(currentQuestions);
    this.saveQuestionsToCache(currentQuestions);
  }

  updateLocalCacheAfterAdd(newQuestion: QuestionDto) {
    this.updateLocalCache(questions => questions.push(newQuestion));
  }

  updateLocalCacheAfterModify(modifiedQuestion: QuestionDto) {
    this.updateLocalCache(questions => {
      const index = questions.findIndex(q => q.id === modifiedQuestion.id);
      if (index > -1) {
        questions[index] = modifiedQuestion;
      }
    });
  }

  updateLocalCacheAfterDelete(id: number) {
    this.updateLocalCache(questions => {
      const index = questions.findIndex(q => q.id === id);
      if (index > -1) {
        questions.splice(index, 1);
      }
    });
  }

  addQuestion(question: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.post<QuestionDto>(environment.addQuestion, question).pipe(
      tap(newQuestion => this.updateLocalCacheAfterAdd(newQuestion))
    );
  }

  modifyQuestion(id: number, question: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.put<QuestionDto>(`${environment.modifyQuestion}${id}`, question).pipe(
      tap(updatedQuestion => this.updateLocalCacheAfterModify(updatedQuestion))
    );
  }

  deleteQuestion(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.deleteQuestion}${id}`).pipe(
      tap(() => this.updateLocalCacheAfterDelete(id))
    );
  }

  updateQuestions(questions: QuestionDto[]) {
    this.questionSubject.next(questions); // mise à jour du BehaviorSubject
    localStorage.setItem('questions_cache', JSON.stringify(questions));
}
}
