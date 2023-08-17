import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, of, } from 'rxjs';
import { map, startWith, switchMap, tap } from 'rxjs/operators';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { FilteredQuestionsData } from 'src/app/share/dtos/qcm/filtered-questions-data';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { Categorie } from 'src/app/share/enums/categorie.enum';
import { Niveau } from 'src/app/share/enums/niveau.enum';
import { Technologie } from 'src/app/share/enums/technologie.enum';

@Component({
  selector: 'app-create-qcm',
  templateUrl: './create-qcm.component.html',
  styleUrls: ['./create-qcm.component.scss']
})
export class CreateQcmComponent implements OnInit, OnDestroy {
  allQuestions$: Observable<QuestionDto[]>;
  qcmFrom!: FormGroup;
  subscriptions: Subscription[] = [];
  latestQuestions: QuestionDto[] = [];

  categories = Object.values(Categorie).map(cat => ({ name: cat }));
  technologies = Object.values(Technologie).map(tech => ({ name: tech }));
  niveaux = Object.values(Niveau).map(niv => ({ name: niv }));
  nbreQuestion = Array.from({ length: 100 }, (_, i) => ({ name: i.toString() }));

  constructor(private questionService: QuestionService,
              private formBuilder: FormBuilder,
              private qcmService: QcmService) {
    const cachedQuestions = localStorage.getItem('questions_cache');
    if (cachedQuestions) {
      this.allQuestions$ = of(JSON.parse(cachedQuestions));
    } else {
      this.allQuestions$ = this.questionService.questions$;
    }
   }

  ngOnInit(): void {
    const cachedQuestions = localStorage.getItem('questions_cache');
    if (!cachedQuestions) {
      const loadSubscription = this.questionService.loadAllQuestions().subscribe();
      this.subscriptions.push(loadSubscription);
    }

    this.qcmFrom = this.formBuilder.group({
      rows: this.formBuilder.array([this.createRow()], this.hasDuplicateRows)
    });

    this.rows.controls.forEach((control: AbstractControl, index: number) => {
      this.ecouterChangements(index);
    });
    this.allQuestions$.subscribe(questions => {
      this.latestQuestions = questions;
    });
  }

  ecouterChangements(indiceLigne: number): void {
    const ligne = this.rows.at(indiceLigne) as FormGroup;
    const subscription = ligne.valueChanges.pipe(
      startWith(ligne.value),
      switchMap(row => {
        return this.allQuestions$.pipe(
          map(questions => {
            let questionsFiltrees = questions;
            if(row.technologie && row.technologie.name) {
              questionsFiltrees = questionsFiltrees.filter(q => q.technologie === row.technologie.name);
            }

            if(row.categorie && row.categorie.name) {
              questionsFiltrees = questionsFiltrees.filter(q => q.categorie === row.categorie.name);
            }

            if(row.niveau && row.niveau.name) {
              questionsFiltrees = questionsFiltrees.filter(q => q.niveau === row.niveau.name);
            }

            return questionsFiltrees.length
          })
        );
      }),
      tap(nombreQuestions => {
        const options = Array.from({ length: nombreQuestions }, (_, i) => ({ name: (i + 1).toString() }));
        this.nbreQuestion = options;
      })
    ).subscribe();
    this.subscriptions.push(subscription);
  }

  get rows(): FormArray {
      return this.qcmFrom.get('rows') as FormArray;
  }

  createRow(): FormGroup {
    return this.formBuilder.group({
      categorie: ['', Validators.required],
      technologie: ['', Validators.required],
      niveau: ['', Validators.required],
      nbreQuestion: ['', Validators.required]
    });
  }

  addRow(): void {
    if(!this.qcmFrom.get('rows')?.hasError('duplicateRows')) {
      this.rows.push(this.createRow());
      this.ecouterChangements(this.rows.length - 1);
    } else {
      console.error('Combinaison de filtre déjà existante');
    }
  }

  subRow(): void {
    const index = this.rows.length -1;
    if(index >= 1)
      this.rows.removeAt(index);
  }

  submitQcm(): void {
    if(this.qcmFrom.get('rows')?.hasError('duplicateRows')) {
      console.error('Combinaison de filtre déjà existante');
      return;
    }
    const formData = this.qcmFrom.value;
    const allFilteredQuestion: FilteredQuestionsData[] = [];
    let grandTotalPoints  = 0;
    let grandTotalTime  = 0;

    formData.rows.forEach((row: {categorie: { name: string }; technologie: { name: string }; niveau: { name: string }; nbreQuestion: {name: string};}) => {
      let filteredQuestions = [...this.latestQuestions];

      if(row.technologie && row.technologie.name)
        filteredQuestions = filteredQuestions.filter((q: QuestionDto) => q.technologie === row.technologie.name);
      if(row.categorie && row.categorie.name)
        filteredQuestions = filteredQuestions.filter((q: QuestionDto) => q.categorie === row.categorie.name);
      if(row.niveau && row.niveau.name)
          filteredQuestions = filteredQuestions.filter((q: QuestionDto) => q.niveau === row.niveau.name);

      const suffledQuestions = this.shuffleArray(filteredQuestions);
      const selectedQuestions = suffledQuestions.slice(0, Number(row.nbreQuestion.name));
      const totalTime = selectedQuestions.reduce((acc, question) => acc + question.temps, 0);
      const totalpoint = selectedQuestions.reduce((acc, question) => acc + question.point, 0);
      //allFilteredQuestion.push(suffledQuestions.slice(0, Number(row.nbreQuestion.name)));

      grandTotalPoints += totalTime;
      grandTotalTime += totalpoint;

      allFilteredQuestion.push({
        questions: selectedQuestions,
        totalTime: totalTime,
        totalPoints: totalpoint
      });
    });
    const qcmData: QcmDto = {
      temps: grandTotalTime,
      point: grandTotalPoints
    }
    this.qcmService.addQcm(qcmData).subscribe( (response: QcmDto) => {
      const newQcmId = response.id;
      allFilteredQuestion.forEach(question => {
        this.qcmService.addQuestionToQcm(newQcmId, question.id).subscribe(
          resp => {
            console.log('Question ajoutée avec succès:', resp);
          },
          error => {
            console.error('Erreur lors de l\'ajout de la question:', error);
          }
        );
      });
      console.log('Données du QCM envoyées avec succès!', response);
    },
    error => {
      console.error('Erreur lors de l\'envoi des données du QCM', error);
      }
    ),
  }

  shuffleArray(array: any[]): any[] {
    for(let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  hasDuplicateRows(controlArray: AbstractControl): {[s: string]: boolean} | null {
    const rows = controlArray.value as Array<any>;
    const uniqueSet = new Set(
      rows.map(row => `${row.categorie.name}-${row.technologie.name}-${row.niveau.name}`)
    );
    if(uniqueSet.size !== rows.length)
      return { duplicateRows: true };
    return null;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
