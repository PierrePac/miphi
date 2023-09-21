import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Niveau } from 'src/app/share/enums/niveau.enum';
import { Technologie } from 'src/app/share/enums/technologie.enum';

@Component({
  selector: 'app-create-sandbox',
  templateUrl: './create-sandbox.component.html',
  styleUrls: ['./create-sandbox.component.scss']
})
export class CreateSandboxComponent implements OnInit {
  sandboxForm!: FormGroup;
  consigne: string = '';
  niveaux = Object.values(Niveau);
  technologies = Object.values(Technologie);

  @Output() onSave: EventEmitter<any> = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.sandboxForm = this.formBuilder.group({
      src: ['', [Validators.required]],
      consigne: [''],
      nom: ['', [Validators.required]],
      niveau: [null, [Validators.required]],
      technologie: [null, [Validators.required]]
    });
  }

  addConsigne() {
    const currentConsigne = this.sandboxForm.get('consigne')?.value || '';
    this.consigne = this.consigne ? `${this.consigne} -- ${currentConsigne}` : currentConsigne;
    this.sandboxForm.get('consigne')?.setValue('');
  }

  onSubmit() {
    if (this.sandboxForm.valid && this.consigne != '') {
      console.log(this.consigne)
      this.sandboxForm.get('consigne')?.patchValue(this.consigne);
      this.onSave.emit(this.sandboxForm.value);
    }
  }
}
