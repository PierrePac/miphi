import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  loginAdmin( nom: string, motDePasse: string ): Observable<any> {
    const body = { nom: nom, motDePasse: motDePasse };
    return this.http.post( environment.adminConnexionUrl, body );
  }

  loginCandidat( nom: string, prenom: string ): Observable<any> {
    const body = { nom: nom, prenom: prenom };
    return this.http.post( environment.candidatConnexionUrl, body );
  }
}
