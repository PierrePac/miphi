import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AxiosService } from 'src/app/core/services/axios/axios.service';

import { Categorie } from 'src/app/share/enums/categorie.enum';
import { Niveau } from 'src/app/share/enums/niveau.enum';
import { Technologie } from 'src/app/share/enums/technologie.enum';
import { Question } from 'src/app/share/models/question/question';

@Component({
  selector: 'app-view-question',
  templateUrl: './view-question.component.html',
  styleUrls: ['./view-question.component.scss']
})
export class ViewQuestionComponent implements OnInit {
  form!: FormGroup;
  categories = Object.values(Categorie);
  technologies = Object.values(Technologie);
  niveaux = Object.values(Niveau);

  questions: Question[] = [];


  constructor(private formbuilder: FormBuilder,
              private axiosService: AxiosService
              ) {}

  ngOnInit(): void {
    this.axiosService.request(
      "GET",
      "/api/questions",
      {}
    ).then(
      (response) => this.questions = response.data
    );

    this.form = this.formbuilder.group({
      temps: [0, [Validators.required, Validators.pattern('^[0-9]*$')]],
      points: [0, [Validators.required, Validators.pattern('^[0-9]*$')]],
      question: [null, [Validators.required]],
      categorie: [null, [Validators.required]],
      technologie: [null, [Validators.required]],
      niveau: [null, [Validators.required]],
    });
  }



  onSubmit() {
    if (this.form.valid) {
      const newQuestion: Question = {
        temps: this.form.get('temps')?.value,
        points: this.form.get('points')?.value,
        question: this.form.get('question')?.value,
        categorie: this.form.get('categorie')?.value,
        technologie: this.form.get('technologie')?.value,
        niveau: this.form.get('niveau')?.value
      };


    }
  }
}
