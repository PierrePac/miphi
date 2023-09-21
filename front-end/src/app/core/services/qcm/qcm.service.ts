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
    // Abonnement à qcms$ pour mettre à jour this.qcms
    this.qcms$.subscribe(data => {
      this.qcms = data;
    });
  }

  // Méthode pour ajouter un QCM
  addQcm(qcmData: QcmDto): Observable<QcmDto> {
    return this.httpClient.post<QcmDto>(environment.addQcm, qcmData).pipe(
      tap(() => this.getQcms().subscribe())
    );
  }

  // Méthode pour ajouter des questions à un QCM
  addQuestionToQcm(qcmId: number, questionIds: number[]): Observable<any> {
    const data = { qcmId, questionIds };
    return this.httpClient.post(environment.addQuestionToQcm, data)
  }

  // Méthode pour obtenir tous les QCMs
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

  // Méthode pour obtenir les questions d'un QCM spécifique
  getQcmQuestion(qcmId: number): Observable<QuestionQcmDto[]> {
    return this.httpClient.get<QuestionQcmDto[]>(`${environment.getQcmQuestion}${qcmId}`)
  }

  // Méthode pour obtenir les questions d'un entretien spécifique
  getQcmQuestionByEntretien(entretienId: number) : Observable<QuestionQcmDto[]> {
    return this.httpClient.get<QuestionQcmDto[]>(`${environment.getQcmQuestionByEntretien}${entretienId}`)
  }

  // Méthode pour obtenir un QCM d'un entretien spécifique
  getQcmByEntretien(entretienId: number): Observable<QcmDto> {
    return this.httpClient.get<QcmDto>(`${environment.getOneQcm}${entretienId}`)
  }

  // Méthode pour obtenir les QCMs actuels stockés localement
  getCurrentQcms(): QcmDto[] {
    return this.qcms;
  }

  // Méthode pour mettre à jour l'ordre des questions dans un QCM
  updateQuestionsOrder(question: QuestionQcmDto[]): Observable<any> {
    return this.httpClient.post(environment.updateOrdreQcmQuestion, question);
  }

}
