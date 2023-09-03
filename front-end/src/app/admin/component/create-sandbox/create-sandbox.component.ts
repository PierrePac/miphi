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

  niveaux = Object.values(Niveau);
  technologies = Object.values(Technologie);

  @Output() onSave: EventEmitter<any> = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.sandboxForm = this.formBuilder.group({
      src: ['', [Validators.required]],
      consigne: ['', [Validators.required]],
      nom: ['', [Validators.required]],
      niveau: [null, [Validators.required]],
      technologie: [null, [Validators.required]]
    });
  }

  onSubmit() {
    if (this.sandboxForm.valid) {
      this.onSave.emit(this.sandboxForm.value);
    }
  }
}
