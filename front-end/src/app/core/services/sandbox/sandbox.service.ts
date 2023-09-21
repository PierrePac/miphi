import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, shareReplay, tap } from 'rxjs';
import { ConsignesOgjDto } from 'src/app/share/dtos/consignes/consignes-obj-dto';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SandboxService {
  private sandboxSubject = new BehaviorSubject<SandboxDto[]>([]);
  sandbox$ = this.sandboxSubject.asObservable().pipe(shareReplay(1));
  private sandbox: SandboxDto[] = [];

  constructor(private httpClient: HttpClient) {
    // Abonnement à l'observable pour mettre à jour le tableau sandbox
    this.sandbox$.subscribe(data => {
      this.sandbox = data;
    })
  }

  // Méthode pour récupérer tous les éléments sandbox depuis l'API
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

  // Méthode pour sauvegarder un nouvel élément sandbox dans l'API
  saveSandbox(data: SandboxDto): Observable<SandboxDto> {
    return this.httpClient.post<SandboxDto>(environment.addSandbox, data);
  }

  // Méthode pour récupérer une consigne basée sur l'id d'un entretien
  getConsigne(idEntretien: number): Observable<ConsignesOgjDto> {
    return this.httpClient.get<ConsignesOgjDto>(`${environment.getConsignes}${idEntretien}`)
  }
}
