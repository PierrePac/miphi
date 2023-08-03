import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoginService } from 'src/app/core/services/login/login.service';
import { Personne } from 'src/app/share/models/personne/personne.model';



@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
  providers: [MessageService]
})
export class LandingPageComponent implements OnInit {
  candidatForm!: FormGroup;
  adminForm!: FormGroup;
  @ViewChild('adminBtn', { static: false }) adminBtn!: ElementRef;
  @ViewChild('candidatBtn', {static: false}) candidatBtn!: ElementRef;
  @ViewChild('container', {static: false}) container!: ElementRef;

  constructor( private formBuilder: FormBuilder,
              private loginService: LoginService,
              private router: Router,
              private messageService: MessageService,
              ) {}

  ngOnInit(): void {
    this.candidatForm = this.formBuilder.group({
      prenom: ['', Validators.required ],
      nom: ['', Validators.required ]
    });

    this.adminForm = this.formBuilder.group ({
      nom: ['', Validators.required],
      motDePasse: ['', Validators.required]
    });
  }

  ngAfterViewInit(): void {
    this.adminBtn.nativeElement.addEventListener('click', () => {
      this.container.nativeElement.classList.remove('right-panel-active');
    });

    this.candidatBtn.nativeElement.addEventListener('click', () => {
      this.container.nativeElement.classList.add('right-panel-active');
    });
  }

  onSubmit(form: FormGroup) {
    if (form.valid) {
      if ( form === this.adminForm ) {
        this.loginService.loginAdmin(form.value.nom.toLowerCase(), form.value.motDePasse.toLowerCase()).subscribe({
          next: (response) => {
            const personne = <Personne>response;
              sessionStorage.setItem('personne', JSON.stringify(personne));
              if (personne.role === 'ADMIN') {
                this.router.navigate(['/admin']);
              }
          },
          error: (err) => {
            this.messageService.add({severity:'error', summary:'Erreur', detail:'Nom ou mot de passe incorrect'});
          }
        });
      } else if (form === this.candidatForm) {
        this.loginService.loginCandidat(form.value.nom.toLowerCase(), form.value.prenom.toLowerCase()).subscribe({
          next: (response) => {
            const personne = <Personne>response;
            sessionStorage.setItem('personne', JSON.stringify(personne));
            if (personne.role === 'CANDIDAT') {
              this.router.navigate(['/candidat']);
            }
          },
          error: (err) =>  {
            this.messageService.add({severity:'error', summary:'Erreur', detail:'Nom ou prénom incorrect'});
          }
        })
      }
    }
  }
}
