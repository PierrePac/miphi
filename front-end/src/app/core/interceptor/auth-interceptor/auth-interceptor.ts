import { Injectable } from "@angular/core";
import { AuthenticationService } from "../../services/authentication/authentication.service";
import { Router } from "@angular/router";
import { HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpRequest } from "@angular/common/http";
import { Observable, catchError, retry, switchMap, throwError } from "rxjs";
import { TokenDto } from "src/app/share/dtos/token/token-dto";

@Injectable()
export class AuthInterceptor {

  constructor(private authService: AuthenticationService,
              private httpClient: HttpClient,
              private router: Router) {}

              intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
                const token = window.localStorage.getItem('auth_token');  // Récupérez votre token depuis là où il est stocké

                if (token) {
                  request = request.clone({
                    setHeaders: {
                      Authorization: `Bearer ${token}`
                    }
                  });
                }

                return next.handle(request);
              }
  }

