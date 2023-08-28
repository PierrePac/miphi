import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EntretienService {
  private entretienSubject = new BehaviorSubject<EntretienDto | null>(null);
  entretien$ = this.entretienSubject.asObservable();

  constructor(private httpClient: HttpClient) {
    this.loadInitialData();
  }

  private loadInitialData() {
    const personneStr = sessionStorage.getItem('personne');
    if (personneStr) {
      const personne = JSON.parse(personneStr);
      const personneId = personne.entretienId;
      if (personneId) {
        this.getEntretienById(Number(personneId)).subscribe(
          (data) => {
            this.entretienSubject.next(data);
            sessionStorage.setItem('entretien', JSON.stringify(data));
          },
          (error) => {
            console.error('Failed to fetch data', error);
          }
        );
      }
    }
  }

  getEntretienById(id: number) {
    return this.httpClient.get<EntretienDto>(`${environment.getEntretien}${id}`);
  }

  createEntretien(data: EntretienDto): Observable<EntretienDto> {
    return this.httpClient.post<EntretienDto>(environment.addEntretien, data)
  }
}
