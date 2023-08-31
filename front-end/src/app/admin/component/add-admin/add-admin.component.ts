import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { AdminDto } from 'src/app/share/dtos/admin/admin-dto';
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
      this.personneService.createAdmin(this.newAdmin).subscribe((resp: any) => {
        console.log(resp)
        this.show('Admin Ajouté à la BDD', 'success')
        setTimeout(() => {
          this.router.navigate(['/admin'], { queryParams: { section: 'sandbox' } });
        }, 1500);
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
