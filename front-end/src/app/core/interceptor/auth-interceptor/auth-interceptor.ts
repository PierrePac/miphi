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

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = window.localStorage.getItem('auth_token');

    if (token && request.url !== environment.refreshToken) {
      request = this.addTokenToRequest(request, token);
    }

    return next.handle(request).pipe(
      catchError(err => {
        if (err.status === 401) {
          return this.handleTokenExpiredError(request, next);
        } else {
          return throwError(err);
        }
      })
    );
  }

  private addTokenToRequest(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  private handleTokenExpiredError(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const refreshToken = window.localStorage.getItem('refresh_token');
    if (!refreshToken) {
      this.router.navigateByUrl('');
      return throwError(new Error('refresh token manquant'));
    }

    return this.httpClient.post<TokenDto>(environment.refreshToken, { refreshToken }).pipe(
      retry(1),
      switchMap((tokens: TokenDto) => {
        window.localStorage.removeItem('auth_token');
        window.localStorage.setItem('auth_token', tokens.token);
        request = this.addTokenToRequest(request, tokens.token);
                    return next.handle(request);
      }),
      catchError((err: any) => {
        this.router.navigateByUrl('');
        return throwError(err);
      })
    )
  }
}

