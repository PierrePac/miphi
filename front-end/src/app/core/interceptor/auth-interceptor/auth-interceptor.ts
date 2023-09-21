import { Injectable } from "@angular/core";
import { AuthenticationService } from "../../services/authentication/authentication.service";
import { Router } from "@angular/router";
import { HttpClient, HttpEvent, HttpHandler, HttpRequest } from "@angular/common/http";
import { Observable, catchError, switchMap, throwError } from "rxjs";
import { retry } from 'rxjs/operators';
import { environment } from "src/environments/environment";
import { TokenDto } from "src/app/share/dtos/token/token-dto";

@Injectable()
export class AuthInterceptor {

  constructor(private authService: AuthenticationService,
              private httpClient: HttpClient,
              private router: Router) { }

  // Méthode qui intercepte chaque requête HTTP
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Récupération du token d'authentification depuis le localStorage
    const token = window.localStorage.getItem('auth_token');

    // Ajout du token au header si le token et l'URL de rafraîchissement du token sont présents
    if (token && request.url !== environment.refreshToken) {
      request = this.addTokenToRequest(request, token);
    }

    // Traitement de la requête et gestion des erreurs
    return next.handle(request).pipe(
      catchError(err => {
        // Si le statut d'erreur est 401 (non autorisé), on gère le rafraîchissement du token
        if (err.status === 401) {
          return this.handleTokenExpiredError(request, next);
        } else {
          // Si une autre erreur se produit, on la renvoie
          return throwError(err);
        }
      })
    );
  }

  // Méthode pour ajouter un token d'authentification à l'entête d'une requête
  private addTokenToRequest(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Méthode pour gérer l'erreur d'expiration du token
  private handleTokenExpiredError(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Récupération du token de rafraîchissement
    const refreshToken = window.localStorage.getItem('refresh_token');
    // Si le token de rafraîchissement est absent, on redirige vers la page d'accueil et on lance une erreur
    if (!refreshToken) {
      this.router.navigateByUrl('');
      return throwError(new Error('refresh token manquant'));
    }

    // Si le token de rafraîchissement est présent, on effectue une requête pour obtenir un nouveau token
    return this.httpClient.post<TokenDto>(environment.refreshToken, { refreshToken }).pipe(
      retry(3), // Tentatives de reconnexion en cas d'échec
      switchMap((tokens: TokenDto) => {
        // Suppression de l'ancien token et ajout du nouveau
        window.localStorage.removeItem('auth_token');
        window.localStorage.setItem('auth_token', tokens.token);
        // Réexécution de la requête initiale avec le nouveau token
        request = this.addTokenToRequest(request, tokens.token);
                    return next.handle(request);
      }),
      catchError((err: any) => {
        // Si une erreur se produit lors du processus de rafraîchissement, on redirige vers la page d'accueil
        this.router.navigateByUrl('');
        return throwError(err);
      })
    )
  }
}

