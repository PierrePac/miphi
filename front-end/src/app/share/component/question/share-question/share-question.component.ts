import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { PropositionDto } from 'src/app/share/dtos/proposition/proposition-dto';
import { Categorie } from 'src/app/share/enums/categorie.enum';
import { Niveau } from 'src/app/share/enums/niveau.enum';
import { Technologie } from 'src/app/share/enums/technologie.enum';

@Component({
  selector: 'app-share-question',
  templateUrl: './share-question.component.html',
  styleUrls: ['./share-question.component.scss']
})
export class ShareQuestionComponent implements OnInit {
  @Input() mode:'edit' | 'display' = 'display';
  @Input() questionDto!: QuestionDto;
  @Output() onSave: EventEmitter<any> = new EventEmitter();
  @Output() onDelete: EventEmitter<any> = new EventEmitter();

  form!: FormGroup;
  categories = Object.keys(Categorie).map(key => ({ label: Categorie[key as keyof typeof Categorie], value: key }));
  technologies = Object.keys(Technologie).map(key => ({ label: Technologie[key as keyof typeof Technologie], value: key }));
  niveaux = Object.keys(Niveau).map(key => ({ label: Niveau[key as keyof typeof Niveau], value: key }));

  correctOptions: any[] = [
    {label: 'Vrai', value: true},
    {label: 'Faux', value: false}
  ]

  get reponses(): FormArray {
    return this.form.get('reponses') as FormArray;
  }

  constructor(private formbuilder: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
    if (this.questionDto) {
      const formData = this.patchFormWithQuestion(this.questionDto);
      this.form.patchValue(formData);
    }
  }

  initForm() {
    this.form = this.formbuilder.group({
      id: [this.questionDto?.id],
      temps: [1, [Validators.required, Validators.pattern('^[0-9]*$')]],
      point: [1, [Validators.required, Validators.pattern('^[0-9]*$')]],
      question: [null, [Validators.required]],
      categorie: [null, [Validators.required]],
      technologie: [null, [Validators.required]],
      niveau: [null, [Validators.required]],
      reponses: this.formbuilder.array([
        this.createReponse(),
        this.createReponse(),
        this.createReponse(),
        this.createReponse(),
        this.createReponse(),
        this.createReponse(),
      ])
    });
  }

  toggleMode() {
    if (this.mode === 'edit') {
      this.mode = 'display';
    } else {
      this.mode = 'edit';
      const formData = this.patchFormWithQuestion(this.questionDto);
      this.form.patchValue(formData);
    }
  }

  patchFormWithQuestion(question: QuestionDto) {
    const formData = {
      id: question.id,
      temps: question.temps,
      point: question.point,
      question: question.question,
      categorie: question.categorie,
      technologie: question.technologie,
      niveau: question.niveau,
      reponses: question.reponses?.map((resp: PropositionDto) => ({
        id: resp.id,
        reponse: resp.reponse,
        correct: resp.correct
      }))
    };

    const reponseFgs: FormGroup[] = question.reponses
      ? question.reponses.map(reponse => this.formbuilder.group({
        id: reponse.id,
        reponse: reponse.reponse,
        correct: reponse.correct
      })) : [];

      if (reponseFgs.length < 6) {
        reponseFgs.push(this.createReponse());
      }

      this.form.setControl('reponses', this.formbuilder.array(reponseFgs));
      reponseFgs.forEach(fg => this.addReponseChangeListener(fg));
      return formData;
  }

  onSubmit() {
    if (this.form.valid) {
      this.onSave.emit(this.form.value);
      this.form.reset();
    }
  }
  onDeleteQuestion(): void {
    this.onDelete.emit(this.questionDto);
  }

  static minRequiredReponses(min = 1): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (!control.value || !Array.isArray(control.value)) {
        return { required: true };
      }
      const filledFields = control.value
        .filter((response: any) => response?.reponse && response.reponse.trim() !== '').length; // Corrigé ici

      return filledFields >= min ? null : { required: true };
    };
  }

  createReponse(): FormGroup {
    return this.formbuilder.group({
      id: null,
      reponse: null,
      correct: false
    });
  }

  private addReponseChangeListener(form: FormGroup): void {
    form.get('reponse')?.valueChanges.subscribe(value => {
      const lastReponseIndex = this.reponses.controls.indexOf(form);
      if (value && lastReponseIndex === this.reponses.length - 1 && this.reponses.length < 6) {
        this.addReponseField();
      }
    })
  }

  addReponseField(): void {
    if(this.reponses.length < 6) {
      const newReponse = this.createReponse();
      this.reponses.push(newReponse);
      this.addReponseChangeListener(newReponse);
    }
  }



}
