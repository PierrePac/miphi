import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, shareReplay, map } from 'rxjs';
import { tap } from 'rxjs/operators';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionQcmDto } from 'src/app/share/dtos/question/question-qcm-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QcmService {
  private qcmSubject = new BehaviorSubject<QcmDto[]>([]);
  qcms$ = this.qcmSubject.asObservable().pipe(
    shareReplay(1)
  );
  private qcms: QcmDto[] = [];

  constructor(private httpClient: HttpClient) {
    this.qcms$.subscribe(data => {
      this.qcms = data;
    });
  }

  addQcm(qcmData: QcmDto): Observable<QcmDto> {
    return this.httpClient.post<QcmDto>(environment.addQcm, qcmData).pipe(
      tap(() => this.getQcms().subscribe())
    );
  }

  addQuestionToQcm(qcmId: number, questionIds: number[]): Observable<any> {
    const data = { qcmId, questionIds };
    return this.httpClient.post(environment.addQuestionToQcm, data)
  }

  getQcms(): Observable<QcmDto[]> {
    const sortQcm = (qcms: QcmDto[]) =>
    qcms.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));

    return this.httpClient.get<QcmDto[]>(environment.getAllQcms).pipe(
      map(qcms => sortQcm(qcms)),
      tap(sortedQcms => {
        this.qcmSubject.next(sortedQcms)
      })
    );
  }

  getQcmQuestion(qcmId: number): Observable<QuestionQcmDto[]> {
    return this.httpClient.get<QuestionQcmDto[]>(`${environment.getQcmQuestion}${qcmId}`)
  }

  getQcmQuestionByEntretien(entretienId: number) : Observable<QuestionQcmDto[]> {
    return this.httpClient.get<QuestionQcmDto[]>(`${environment.getQcmQuestionByEntretien}${entretienId}`)
  }

  getQcmByEntretien(entretienId: number): Observable<QcmDto> {
    return this.httpClient.get<QcmDto>(`${environment.getOneQcm}${entretienId}`)
  }

  getCurrentQcms(): QcmDto[] {
    return this.qcms;
  }

  updateQuestionsOrder(question: QuestionQcmDto[]): Observable<any> {
    return this.httpClient.post(environment.updateOrdreQcmQuestion, question);
  }

}
