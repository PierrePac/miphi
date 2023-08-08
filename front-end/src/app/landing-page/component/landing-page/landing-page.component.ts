import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AxiosService } from 'src/app/core/services/axios/axios.service';
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
              private router: Router,
              private axiosService: AxiosService,
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
    if(this.axiosService.getAuthToken != null) {
      window.localStorage.removeItem("auth_token");
    }
    if (form.valid) {
      let requestPromise;
      if (form === this.adminForm) {
        requestPromise = this.axiosService.request(
          "POST",
          "/login/admin",
          {
            nom: form.value.nom.toLowerCase(),
            motDePasse: form.value.motDePasse.toLowerCase()
          }
        ).then(response => {
          this.axiosService.setAuthToken(response.data.token);
          const personne = <Personne>response.data
          sessionStorage.setItem('personne', JSON.stringify(personne));
          if (personne.role === 'ADMIN') {
            this.router.navigate(['/admin']);
          }
        });
      } else if (form === this.candidatForm) {
        requestPromise = this.axiosService.request(
          "POST",
          "/login/candidat",
          {
            nom: form.value.nom.toLowerCase(),
            prenom: form.value.prenom.toLowerCase()
          }
        ).then(response => {
          this.axiosService.setAuthToken(response.data.token);
          this.axiosService.setAuthToken(response.data.token);
          const personne = <Personne>response.data
          sessionStorage.setItem('personne', JSON.stringify(personne));
          if (personne.role === 'CANDIDAT') {
            this.router.navigate(['/candidat']);
          }
        });
      }
    }
  }
}

