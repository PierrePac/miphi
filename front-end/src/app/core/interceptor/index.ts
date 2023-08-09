import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { retry } from 'rxjs';
import { AuthInterceptor } from './auth-interceptor/auth-interceptor';

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  retry
];
