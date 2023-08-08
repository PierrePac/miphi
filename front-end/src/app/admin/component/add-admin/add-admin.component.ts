import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AxiosService } from 'src/app/core/services/axios/axios.service';


@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.scss']
})
export class AddAdminComponent implements OnInit{

  addAdminForm!: FormGroup;

  constructor(private formbuilder: FormBuilder,
              private axiosService: AxiosService) {}

  ngOnInit(): void {
    this.addAdminForm = this.formbuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      motDePasse:['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit() {
    if(this.addAdminForm.valid) {
      this.axiosService.request(
        "POST",
        "/login/add-admin",
        {
          nom: this.addAdminForm.value.nom.toLowerCase(),
          prenom: this.addAdminForm.value.prenom.toLowerCase(),
          motDePasse: this.addAdminForm.value.motDePasse
        }
      )
    }
  }
}
