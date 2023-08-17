import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QcmService {

  constructor(private httpClient: HttpClient) { }

  addQcm(qcmData: QcmDto): Observable<QcmDto> {
    console.log(qcmData)
    return this.httpClient.post<QcmDto>(environment.addQcm, qcmData);
  }

  addQuestionToQcm(qcmId: number, questionId: number) {
    const data = { qcmId, questionId };
    return this.httpClient.post(environment.addQuestionToQcm, data)
  }
}
