import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';

@Component({
  selector: 'app-create-candidat',
  templateUrl: './create-candidat.component.html',
  styleUrls: ['./create-candidat.component.scss']
})
export class CreateCandidatComponent implements OnInit{

  createCandidatForm!: FormGroup;
  private allCandidatsSubject$: BehaviorSubject<CandidatDto[]> = new BehaviorSubject<CandidatDto[]>([]);
  public allCandidats$ = this.allCandidatsSubject$.asObservable();
  candidats: CandidatDto[] = [];
  idAdmin: number | null = null;
  entretienSwitch: boolean = false;
  allQcms$: Observable<QcmDto[]> = of([]);
  selectedQcm?: QcmDto;

  constructor(private qcmService: QcmService,
    private entretienService: EntretienService,
    private personneService: PersonneService,
    private questionService: QuestionService,
    private reponseService: ReponseService,
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

    EntretienToggle() {
      this.entretienSwitch = !this.entretienSwitch;
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

  fetchAllCandidats() {
    this.personneService.getCandidats().subscribe(candidats => {
      this.allCandidatsSubject$.next(candidats);
      this.candidats = candidats;
    });
  }

  onSubmit() {
    const formValues = this.createCandidatForm.value;

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
        qcm: { id: formValues.qcm.id },
        sandbox: { id: formValues.sandbox.id }
      };

      this.entretienService.createEntretien(entretienData).subscribe((entretien: EntretienDto) => {
        if (entretien?.id !== undefined) {
        const candidatData = {
          nom: formValues.nom.toLowerCase(),
          prenom: formValues.prenom.toLowerCase(),
          entretienId: entretien.id
        };
          this.personneService.createCandidat(candidatData).subscribe(response => {
        });
      }
      });
    } else if (formValues.entretien) {
      const candidatData = {
        nom: formValues.nom.toLowerCase(),
        prenom: formValues.prenom.toLowerCase(),
        entretienId: formValues.entretien.id
      };
      this.personneService.createCandidat(candidatData).subscribe(response => {
      });
    }
    this.createCandidatForm.reset();
  }

}
