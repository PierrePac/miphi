import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { NewAdminDto } from 'src/app/share/dtos/admin/new-admin-dto';

@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.scss'],
  providers: [MessageService]
})
export class AddAdminComponent implements OnInit{

  addAdminForm!: FormGroup;
  newAdmin!: NewAdminDto;

  constructor(private formbuilder: FormBuilder,
              private personneService: PersonneService,
              private messageService: MessageService,
              private router: Router) {}

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
      this.newAdmin.nom = this.newAdmin.nom.toLowerCase();
      this.personneService.createAdmin(this.newAdmin).subscribe(response => {
        this.show(response.message, 'success')
        setTimeout(() => {
          this.router.navigate(['/admin'], { queryParams: { section: 'sandbox' } });
        }, 1500);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 400) {
          this.show(error.error.message, 'error')
          console.error('Erreur 400 :', error.error.message);
        } else{
          this.show(error.error.message, 'error')
        }
      });
    } else {
      this.show('Formulaire mal rempli.', 'warning')
    }
  }

  show(message: string, type: string) {
    if(type === 'error')
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: message });
    if(type === 'warning')
    this.messageService.add({ severity: 'warn', summary: 'warn', detail: message });
    if(type === 'success')
    this.messageService.add({ severity: 'success', summary: 'success', detail: message });
  }
}
