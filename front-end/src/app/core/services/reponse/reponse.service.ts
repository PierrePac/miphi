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

  addReponse(newReponse: PropositionDto): Observable<PropositionDto> {
    return this.httpClient.post<PropositionDto>(environment.addReponse, newReponse);
  }

  modifyReponse(reponseId: number, reponse: PropositionDto): Observable<PropositionDto> {
    return this.httpClient.put<PropositionDto>(`${environment.modifyReponse}${reponseId}`, reponse);
  }

  getReponsesCandidat(candidatId: number): Observable<ReponseQcmDto[]> {
    return this.httpClient.get<ReponseQcmDto[]>(`${environment.getQcmReponsesOfCandidat}${candidatId}`)
  }
}
