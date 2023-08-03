import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { QuestionService } from 'src/app/core/services/question/question.service';
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

  constructor(private formbuilder: FormBuilder,
              private questionService: QuestionService,) {}

  ngOnInit(): void {
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
      this.questionService.addQuestion(newQuestion).subscribe(
        (data) => {
          console.log("Question ajouté avec succés");
          this.form.reset();
        },
        (error) => {
          console.error("Erreur dans l'ajout de la question")
        }
      )
    }
  }
}
