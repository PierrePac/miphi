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

  // Vérifie si l'utilisateur est connecté en consultant le localStorage
  checkIfLoggedIn(): boolean {
    const user = window.localStorage.getItem('auth_token');
    return !!user;
  }

  // Récupère le token d'authentification du localStorage
  getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

  // Création d'un BehaviorSubject pour suivre le statut de connexion de l'utilisateur
  isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.checkIfLoggedIn());

  // Stocke ou supprime le token d'authentification et le token de rafraîchissement dans le localStorage
  setAuthToken(token: string | null, refreshToken: string | null): void {
    if (token !== null && refreshToken !==null) {
      window.localStorage.setItem("auth_token", token);
      window.localStorage.setItem('refresh_token', refreshToken);
    } else {
      window.localStorage.removeItem("auth_token");
    }
  }

  // Authentification pour les administrateurs
  // Prend en argument un objet avec les champs 'nom' et 'motDePasse', et renvoie un Observable d'AdminDto
  authenticateAdmin(data: { nom: string, motDePasse: string }): Observable<AdminDto> {
    return this.httpClient.post<AdminDto>(environment.adminConnexionUrl, data);
  }

  // Authentification pour les candidats
  // Prend en argument un objet avec les champs 'nom' et 'prenom', et renvoie un Observable de CandidatDto
  authenticateCandidat(data: { nom: string, prenom: string }): Observable<CandidatDto> {
    return this.httpClient.post<CandidatDto>(environment.candidatConnexionUrl, data);
  }
}
