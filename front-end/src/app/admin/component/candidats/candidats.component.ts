import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { filter } from 'rxjs/operators';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';

@Component({
  selector: 'app-candidats',
  templateUrl: './candidats.component.html',
  styleUrls: ['./candidats.component.scss']
})
export class CandidatsComponent implements OnInit{
  @Input() mode:'view-candidats' | 'create-candidat' = 'create-candidat';
  allQcms$: Observable<QcmDto[]> = of([]);
  createCandidatForm!: FormGroup;
  entretienSwitch: boolean = false;
  selectedQcm?: QcmDto;
  idAdmin: number | null = null;
  private allCandidatsSubject$: BehaviorSubject<CandidatDto[]> = new BehaviorSubject<CandidatDto[]>([]);
  public allCandidats$ = this.allCandidatsSubject$.asObservable();

  constructor(private qcmService: QcmService,
              private entretienService: EntretienService,
              private personneService: PersonneService,
              private formBuilder: FormBuilder) {
    this.allQcms$ = this.qcmService.qcms$;
  }

  ngOnInit(): void {
    this.fetchAllCandidats();
    this.qcmService.getQcms().subscribe();
    this.createCandidatForm = this.formBuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      qcm: [''],
      sandbox: [''],
      entretien: ['']
    }, { validator: this.entretienValidation });
  }

  toggleMode() {
    if (this.mode === 'create-candidat') {
      this.mode = 'view-candidats';
    } else {
      this.mode = 'create-candidat';
    }
  }

  entretienValidation(control: AbstractControl): { [key: string]: any } | null {
    const qcm = control.get('qcm');
    const sandbox = control.get('sandbox');
    const entretien = control.get("entretien");

    if(qcm?.value && sandbox?.value) {
      return null;
    } else if (entretien?.value) {
      return null;
    } else {
      return { 'Erreur de validation': true };
    }
  }

  onSubmit() {
    const formValues = this.createCandidatForm.value;
console.log(formValues)
    if (formValues.qcm && formValues.sandbox) {
      const currentDate = new Date();
      const date_end = new Date(currentDate);
      date_end.setDate(currentDate.getDate() + 30);

      const date_start = currentDate.toISOString();
      const dateEndFormatted = date_end.toISOString();

      const personneStr = sessionStorage.getItem('personne');
      if (personneStr) {
        const personne = JSON.parse(personneStr);
        this.idAdmin = personne.id;
      }

      const entretienData = {
        admin: {
          id: this.idAdmin
        },
        date_end: dateEndFormatted,
        date_start: date_start,
        qcm_id: formValues.qcm,
        sandbox_id: formValues.sandbox
      };

      this.entretienService.createEntretien(entretienData).subscribe((entretien: EntretienDto) => {
        const candidatData = {
          nom: formValues.nom,
          prenom: formValues.prenom,
          entretienId: entretien.id
        };
        this.personneService.createCandidat(candidatData).subscribe(response => {
          // Handle the response or errors from createCandidat here if necessary
        });
      });
    } else if (formValues.entretien) { // Handle the case where only entretien is provided, and qcm & sandbox are not.
      const candidatData = {
        nom: formValues.nom,
        prenom: formValues.prenom,
        entretienId: formValues.entretien.id
      };
      this.personneService.createCandidat(candidatData).subscribe(response => {
        // Handle the response or errors from createCandidat here if necessary
      });
    }
  }

  EntretienToggle() {
    this.entretienSwitch = !this.entretienSwitch;
}

  // sandbox et entretien data

  sandboxOptions = [
    {id: 1, name: 'Sandbox 1', code: 'S1'},
    {id: 2, name: 'Sandbox 2', code: 'S2'},
    // ... autres options ...
  ];

  entretienOptions = [
    {id: 1, name: 'Entretien 1', code: 'E1'},
    {id: 2, name: 'Entretien 2', code: 'E2'},
    // ... autres options ...
  ];


  fetchAllCandidats() {
    this.personneService.getCandidats().subscribe(candidats => {
      this.allCandidatsSubject$.next(candidats);
    })
  }

}
