import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, shareReplay, tap } from 'rxjs';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SandboxService {
  private sandboxSubject = new BehaviorSubject<SandboxDto[]>([]);
  sandbox$ = this.sandboxSubject.asObservable().pipe(
    shareReplay(1)
  );
  private sandbox: SandboxDto[] = [];

  constructor(private httpClient: HttpClient) {
    this.sandbox$.subscribe(data => {
      this.sandbox = data;
    })
   }

   getAllSandbox(): Observable<SandboxDto[]> {
    const sortSandbox = (sandbox: SandboxDto[]) =>
      sandbox.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));

      return this.httpClient.get<SandboxDto[]>(environment.getAllSandbox).pipe(
        map(sandbox => sortSandbox(sandbox)),
        tap(sortedSandbox => {
          this.sandboxSubject.next(sortedSandbox)
        })
      );
   }

   saveSandbox(data: SandboxDto): Observable<SandboxDto> {
    return this.httpClient.post<SandboxDto>(environment.addSandbox, data);
   }
}
