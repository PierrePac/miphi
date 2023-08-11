import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReponseDto } from 'src/app/share/dtos/reponse/reponse-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReponseService {

  constructor(private httpClient: HttpClient) { }

  addReponse(newReponse: ReponseDto): Observable<ReponseDto> {
    return this.httpClient.post<ReponseDto>(environment.addReponse, newReponse);
  }
}
