import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { NewAdminDto } from 'src/app/share/dtos/admin/new-admin-dto';



@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.scss']
})
export class AddAdminComponent implements OnInit{

  addAdminForm!: FormGroup;
  newAdmin!: NewAdminDto;

  constructor(private formbuilder: FormBuilder,
              private personneService: PersonneService) {}

  ngOnInit(): void {
    this.addAdminForm = this.formbuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      motDePasse:['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit() {
    if(this.addAdminForm.valid){
      this.newAdmin = this.addAdminForm.value;
      this.personneService.createAdmin(this.newAdmin).subscribe();
      console.log(this.newAdmin)
    }
  }
}
