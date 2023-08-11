import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';



@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.scss']
})
export class AddAdminComponent implements OnInit{

  addAdminForm!: FormGroup;

  constructor(private formbuilder: FormBuilder,
) {}

  ngOnInit(): void {
    this.addAdminForm = this.formbuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      motDePasse:['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit() {
    
  }
}
