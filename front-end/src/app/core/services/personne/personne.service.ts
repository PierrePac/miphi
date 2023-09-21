import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewAdminDto } from 'src/app/share/dtos/admin/new-admin-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { ServerReponseDto } from 'src/app/share/dtos/server-reponse/server-reponse-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PersonneService {

  constructor(private httpClient: HttpClient) { }

  // Méthode pour créer un nouveau candidat
  // Retourne un Observable qui contient le candidat créé
  createCandidat(data: CandidatDto): Observable<CandidatDto> {
    return this.httpClient.post<CandidatDto>(environment.addCandidat, data);
  }

  // Méthode pour obtenir tous les candidats
  // Retourne un Observable contenant un tableau de tous les candidats
  getCandidats(): Observable<CandidatDto[]> {
    return this.httpClient.get<CandidatDto[]>(environment.getAllCandidats);
  }

  // Méthode pour créer un nouvel administrateur
  // Retourne un Observable contenant la réponse du serveur
  createAdmin(data: NewAdminDto): Observable<ServerReponseDto> {
    return this.httpClient.post<ServerReponseDto>(environment.addAdmin, data)
  }

}
