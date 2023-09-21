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

  // Fonction permettant à un candidat de soumettre ses réponses pour un QCM.
  // Elle utilise une requête POST pour envoyer les réponses du candidat à l'API backend.
  postQcmAnswer(reponsesQcm: ReponseQcmDto[]): Observable<ReponseQcmDto[]> {
    return this.httpClient.post<ReponseQcmDto[]>(environment.postQcmReponses, reponsesQcm);
  }
}
