import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { ReponseDto } from 'src/app/share/dtos/reponse/reponse-dto';

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
  question$: Observable<QuestionDto[]> = this.questionService.question$;
  categories = Object.values(Categorie);
  technologies = Object.values(Technologie);
  niveaux = Object.values(Niveau);

  questions: Question[] = [];
  get reponses(): FormArray {
    return this.form.get('responses') as FormArray;
  }

  constructor(private formbuilder: FormBuilder,
              private questionService: QuestionService,
              private reponseService: ReponseService) {}

  ngOnInit(): void {
    this.questionService.getAllQuestions();

    this.form = this.formbuilder.group({
      temps: [0, [Validators.required, Validators.pattern('^[0-9]*$')]],
      points: [0, [Validators.required, Validators.pattern('^[0-9]*$')]],
      question: [null, [Validators.required]],
      categorie: [null, [Validators.required]],
      technologie: [null, [Validators.required]],
      niveau: [null, [Validators.required]],
    });

    this.form.addControl('responses', new FormArray([], ViewQuestionComponent.minRequiredReponses(1)));
    this.addReponseField();

    this.reponses.valueChanges.subscribe((values: any[]) => {
      const lastReponse = values[values.length - 1];
      if (lastReponse?.text && this.reponses.length < 6 && !this.reponses.at(this.reponses.length - 1)?.value.text) {
        this.addReponseField();
      }
   })
  }

  static minRequiredReponses(min = 1): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        if (!control.value || !Array.isArray(control.value)) {
            return { required: true };
        }
        const filledFields = control.value
            .filter((response: any) => response && response.text && response.text.trim() !== '').length;

        return filledFields >= min ? null : { required: true };
    };
  }


  createReponse(): FormGroup {
    return this.formbuilder.group({
      text: null,
      correct: false
    });
  }

  addReponseField(): void {
    if (this.reponses.length < 6) {
      const reponseGroup = this.createReponse();
      reponseGroup.get('text')?.clearValidators();
      this.reponses.push(this.createReponse());
    }
  }


  onSubmit() {
    if (this.form.valid) {
      const newQuestion: QuestionDto = {
        temps: this.form.get('temps')?.value,
        point: this.form.get('points')?.value,
        question: this.form.get('question')?.value,
        categorie: this.form.get('categorie')?.value,
        technologie: this.form.get('technologie')?.value,
        niveau: this.form.get('niveau')?.value,
      };

      this.questionService.addQuestion(newQuestion).subscribe(addedQuestion => {
        console.log('QuestionAjoutée avec succès', addedQuestion);

        const reponseToAdd = this.form.get('reponses')?.value || [];
        reponseToAdd.forEach((reponse: ReponseDto) => {
          reponse.questionId = addedQuestion.id;
          this.reponseService.addReponse(reponse).subscribe(addedReponse => {
            console.log('Réponse ajoutée avec succès', addedReponse);
            }, error => {
              console.error('Erreurlors de l\'ajout de la réponse', error);
            }
          );
        });
        this.questionService.getAllQuestions;

        }, error => {
          console.error('Erreur lors de l\'ajout de la question', error);
        });
    }
  }
}
