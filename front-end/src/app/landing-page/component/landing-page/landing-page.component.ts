import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from 'src/app/core/services/login.service';



@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  public candidatForm!: FormGroup;
  public adminForm!: FormGroup;
  @ViewChild('adminBtn', { static: false }) adminBtn!: ElementRef;
  @ViewChild('candidatBtn', {static: false}) candidatBtn!: ElementRef;
  @ViewChild('container', {static: false}) container!: ElementRef;

  constructor( private formBuilder: FormBuilder,
              private loginService: LoginService
              ) {}

  ngOnInit(): void {
    this.candidatForm = this.formBuilder.group({
      prenom: ['', Validators.required],
      nom: ['', Validators.required]
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
        this.loginService.loginAdmin(form.value.nom, form.value.motDePasse).subscribe(response => {
          console.log(response);
        });
      } else if (form === this.candidatForm) {
        this.loginService.loginCandidat(form.value.nom, form.value.prenom).subscribe(response => {
          console.log(response);
        })
      }
    }
  }
}
