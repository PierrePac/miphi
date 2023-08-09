import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AdminDto } from 'src/app/share/dtos/admin/admin-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { TokenDto } from 'src/app/share/dtos/token/token-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private httpClient: HttpClient,
              private router: Router) { }

  getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

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

  refreshToken(data: {refresh_token: string}): Observable<TokenDto>{
    const headers = new HttpHeaders().set('X-Skip-Interceptor', `true`);
    const options = { headers: headers }
    return this.httpClient.post<TokenDto>(environment.refreshToken, data, options)
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('refresh_token');
    this.router.navigateByUrl('');
  }
}
