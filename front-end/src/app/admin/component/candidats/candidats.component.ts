import { Component } from '@angular/core';

@Component({
  selector: 'app-candidats',
  templateUrl: './candidats.component.html',
  styleUrls: ['./candidats.component.scss']
})
export class CandidatsComponent {

  public mode: 'view-result' | 'create-candidat' = 'view-result';

  toggleMode(): void {
    this.mode = this.mode === 'create-candidat' ? 'view-result' : 'create-candidat';
  }

}
