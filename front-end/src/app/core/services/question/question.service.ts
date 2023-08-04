import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Question } from 'src/app/share/models/question/question';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor(private http: HttpClient) {}

  addQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>( environment.addOrGetAllQuestionUrl, question, { withCredentials: true } );
  }

  getAllquestions(): Observable<Question[]> {
    return this.http.get<Question[]>(environment.addOrGetAllQuestionUrl, { withCredentials: true }).pipe(
      map((data: any[]) => data.map(item => new Question(item)))
    );
  }
}
