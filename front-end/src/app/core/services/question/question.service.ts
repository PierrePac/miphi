import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Question } from 'src/app/share/models/question/question';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor(private http: HttpClient) {}

  addQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>( environment.addQuestionUrl, question, { withCredentials: true } );
  }
}
