import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  public userForm!: FormGroup;
  public adminForm!: FormGroup;
  @ViewChild('signInBtn', { static: false }) signInBtn!: ElementRef;
  @ViewChild('signUpBtn', {static: false}) signUpBtn!: ElementRef;
  @ViewChild('form1', {static: false}) firstForm!: NgForm;
  @ViewChild('form2', {static: false}) secondForm!: NgForm;
  @ViewChild('container', {static: false}) container!: ElementRef;
  
  
 

  constructor( private formBuilder: FormBuilder ) {}

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required]
    });

    this.adminForm = this.formBuilder.group ({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngAfterViewInit(): void {
    this.signInBtn.nativeElement.addEventListener('click', () => {
      this.container.nativeElement.classList.remove('right-panel-active');
    });
  
    this.signUpBtn.nativeElement.addEventListener('click', () => {
      this.container.nativeElement.classList.add('right-panel-active');
    });
  
    this.firstForm.ngSubmit.subscribe((event) => event.preventDefault());
    this.secondForm.ngSubmit.subscribe((event) => event.preventDefault());
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log(form.value);
    }
  }
}
