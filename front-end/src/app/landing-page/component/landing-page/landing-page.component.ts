import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthenticationService } from 'src/app/core/services/authentication/authentication.service';
import { AdminDto } from 'src/app/share/dtos/admin/admin-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';




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
              private router: Router,
              private authService: AuthenticationService,
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
    if(this.authService.getAuthToken() !== null) {
      window.localStorage.removeItem("auth_token");
    }
    if(form.valid) {
      if(form === this.adminForm) {
        this.authService.authenticateAdmin({
          nom: form.value.nom.toLowerCase(),
          motDePasse: form.value.motDePasse
        }).subscribe(response => {
          this.handleLoginResponse(response);
        });
      } else if (form === this.candidatForm) {
        this.authService.authenticateCandidat({
          nom: form.value.nom.toLowerCase(),
          prenom: form.value.prenom.toLowerCase()
        }).subscribe(response => {
          this.handleLoginResponse(response);
        })
      }
    }
  }

  handleLoginResponse(reponse: AdminDto | CandidatDto) {
    this.authService.setAuthToken(reponse.token, reponse.refreshToken);
    sessionStorage.setItem("personne", JSON.stringify(reponse));
    this.authService.isLoggedIn.next(true);
    if(reponse.role === 'ADMIN') {
      this.router.navigate(['/admin']);
    } else if(reponse.role === 'CANDIDAT') {
      this.router.navigate(['/candidat']);
    }
  }

}
