import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AdminDto } from 'src/app/share/dtos/admin/admin-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';


import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private httpClient: HttpClient) { }

  checkIfLoggedIn(): boolean {
    const user = window.localStorage.getItem('auth_token');
    return !!user;
  }

  getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

  isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.checkIfLoggedIn());

  setAuthToken(token: string | null, refreshToken: string | null): void {
    if (token !== null && refreshToken !==null) {
      window.localStorage.setItem("auth_token", token);
      window.localStorage.setItem('refresh_token', refreshToken);
    } else {
      window.localStorage.removeItem("auth_token");
    }
  }

  authenticateAdmin(data: { nom: string, motDePasse: string }): Observable<AdminDto> {
    return this.httpClient.post<AdminDto>(environment.adminConnexionUrl, data);
  }

  authenticateCandidat(data: { nom: string, prenom: string }): Observable<CandidatDto> {
    return this.httpClient.post<CandidatDto>(environment.candidatConnexionUrl, data);
  }
}
