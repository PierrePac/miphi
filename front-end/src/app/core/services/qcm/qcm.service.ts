import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, shareReplay } from 'rxjs';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
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
    console.log(qcmData)
    return this.httpClient.post<QcmDto>(environment.addQcm, qcmData);
  }

  addQuestionToQcm(qcmId: number, questionIds: number[]): Observable<any> {
    const data = { qcmId, questionIds };
    console.log(data);
    return this.httpClient.post(environment.addQuestionToQcm, data)
  }

  getQcms(): void {
    this.httpClient.get<QcmDto[]>(environment.getAllQcms).subscribe(data => {
      this.qcmSubject.next(data);
    })
  }

  getCurrentQcms(): QcmDto[] {
    return this.qcms;
  }
}
