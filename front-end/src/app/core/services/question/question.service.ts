import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private questionSubject = new BehaviorSubject<QuestionDto[]>([]);
  question$ = this.questionSubject.asObservable();

  constructor(private httpClient: HttpClient) { }

  getAllQuestions(): Observable<QuestionDto[]> {
    return this.httpClient.get<QuestionDto[]>(environment.GetAllQuestionUrl);
  }

  addQuestion(newQuestion: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.post<QuestionDto>(environment.addQuestion, newQuestion);
  }

  modifyQuestion(questionId: number, question: QuestionDto): Observable<QuestionDto> {
    return this.httpClient.put<QuestionDto>(`${environment.modifyQuestion}${questionId}`, question);
  }
}
