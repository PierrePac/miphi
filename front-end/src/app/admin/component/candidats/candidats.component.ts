import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { filter } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';

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


  constructor(private qcmService: QcmService,
              private formBuilder: FormBuilder) {
    this.allQcms$ = this.qcmService.qcms$;
  }

  ngOnInit(): void {
    this.qcmService.getQcms().subscribe();
    console.log(this.allQcms$)
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
    console.log(this.createCandidatForm);
  }

  EntretienToggle() {
    if(this.entretienSwitch)
      this.entretienSwitch = false;
    else
      this.entretienSwitch = true
  }

  // sandbox et entretien data

  sandboxOptions = [
    { name: 'Sandbox 1', code: 'S1' },
    { name: 'Sandbox 2', code: 'S2' },
    // ... autres options ...
  ];

  entretienOptions = [
    { name: 'Entretien 1', code: 'E1' },
    { name: 'Entretien 2', code: 'E2' },
    // ... autres options ...
  ];

}
