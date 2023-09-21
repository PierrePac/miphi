import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PropositionDto } from 'src/app/share/dtos/proposition/proposition-dto';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReponseService {

  constructor(private httpClient: HttpClient) { }

  // Fonction pour ajouter une réponse en utilisant une requête POST
  addReponse(newReponse: PropositionDto): Observable<PropositionDto> {
    return this.httpClient.post<PropositionDto>(environment.addReponse, newReponse);
  }

  // Fonction pour modifier une réponse existante en utilisant une requête PUT
  modifyReponse(reponseId: number, reponse: PropositionDto): Observable<PropositionDto> {
    return this.httpClient.put<PropositionDto>(`${environment.modifyReponse}${reponseId}`, reponse);
  }

  // Fonction pour obtenir les réponses d'un candidat spécifique en utilisant une requête GET
  getReponsesCandidat(candidatId: number): Observable<ReponseQcmDto[]> {
    return this.httpClient.get<ReponseQcmDto[]>(`${environment.getQcmReponsesOfCandidat}${candidatId}`)
  }
}
