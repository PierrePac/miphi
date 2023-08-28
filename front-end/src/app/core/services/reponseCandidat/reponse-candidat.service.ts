import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReponseCandidatService {

  constructor(private httpClient: HttpClient) { }

  postQcmAnswer(reponsesQcm: ReponseQcmDto[]): Observable<ReponseQcmDto[]> {
    return this.httpClient.post<ReponseQcmDto[]>(environment.postQcmReponses, reponsesQcm);
  }
}
