import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { MessageService } from 'primeng/api';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';
import { SandboxService } from 'src/app/core/services/sandbox/sandbox.service';
@Component({
  selector: 'app-create-candidat',
  templateUrl: './create-candidat.component.html',
  styleUrls: ['./create-candidat.component.scss'],
  providers: [MessageService]
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
  allSandbox$: Observable<SandboxDto[]> = of([]);
  selectedSandbox?: SandboxDto;
  allEntretien$: Observable<EntretienDto[]> = of([]);
  selectedEntretien?: EntretienDto;

  constructor(private qcmService: QcmService,
              private entretienService: EntretienService,
              private personneService: PersonneService,
              private formBuilder: FormBuilder,
              private messageService: MessageService,
              private sandboxService: SandboxService,) {
      this.allQcms$ = this.qcmService.qcms$;
      this.allSandbox$ = this.sandboxService.sandbox$;
      this.allEntretien$ = this.entretienService.allEntretien$;
  }


  ngOnInit(): void {
    this.fetchAllCandidats();
    this.qcmService.getQcms().subscribe();
    this.sandboxService.getAllSandbox().subscribe();
    this.entretienService.getAllEntretien().subscribe();
    this.createCandidatForm = this.formBuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      nomEntretien: [''],
      qcm: [''],
      sandbox: [''],
      entretien: [''],
      dateEnd: ['']
    }, { validator: this.entretienValidation });
  }

  show(message: string, type: string) {
    if(type === 'error')
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: message });
    if(type === 'warning')
    this.messageService.add({ severity: 'warn', summary: 'warn', detail: message });
    if(type === 'success')
    this.messageService.add({ severity: 'success', summary: 'success', detail: message });
  }

    onQcmChange(event: any) {
      this.selectedQcm = event.value;
    }
    onSandboxChange(event: any) {
      this.selectedSandbox = event.value;
    }
    onEntretienChange(event: any) {
      this.selectedEntretien = event.value;
    }

    EntretienToggle() {
      this.entretienSwitch = !this.entretienSwitch;
      this.createCandidatForm.get('nomEntretien')?.reset();
      this.createCandidatForm.get('qcm')?.reset();
      this.createCandidatForm.get('sandbox')?.reset();
      this.createCandidatForm.get('entretien')?.reset();
      this.createCandidatForm.get('dateEnd')?.reset();
  }

  entretienValidation(control: AbstractControl): { [key: string]: any } | null {
    const qcm = control.get('qcm');
    const sandbox = control.get('sandbox');
    const nomEntretien = control.get('nomEntretien');
    const entretien = control.get("entretien");

    if(qcm?.value && sandbox?.value && nomEntretien?.value) {
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
      if (formValues.qcm && formValues.sandbox && formValues.nomEntretien && formValues.dateEnd) {
        const currentDate = new Date();

        const date_start = currentDate.toISOString();
        const dateEndFormatted = formValues.dateEnd.toISOString();

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
          sandbox: { id: formValues.sandbox.id },
          nom: formValues.nomEntretien
        };

        this.entretienService.createEntretien(entretienData).subscribe((entretien: EntretienDto) => {
          if (entretien?.id !== undefined) {
            const candidatData = {
              nom: formValues.nom.toLowerCase(),
              prenom: formValues.prenom.toLowerCase(),
              entretienId: entretien.id
            };
            this.show('entretien crée avec succès', 'success')
            this.personneService.createCandidat(candidatData).subscribe(response => {
              this.show('Candidat ajouté avec succès', 'success')
            },
            error => {
              this.show(error.error.message, 'error')
            });
          }
        },
        error => {
          this.show(error.error.message, 'error')
        });
      } else if (formValues.entretien) {
        const candidatData = {
          nom: formValues.nom.toLowerCase(),
          prenom: formValues.prenom.toLowerCase(),
          entretienId: formValues.entretien.id
        };
        this.personneService.createCandidat(candidatData).subscribe(response => {
          this.show('Candidat Ajouté avec succès', 'success');
          this.createCandidatForm.reset();
        },
        error => {
          this.show(error.error.message, 'error');
        });
      } else
      this.createCandidatForm.reset();

  }

}
