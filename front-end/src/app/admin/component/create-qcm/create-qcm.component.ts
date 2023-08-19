import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, of} from 'rxjs';
import { map, startWith, switchMap, tap } from 'rxjs/operators';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { FilteredQuestionsData } from 'src/app/share/dtos/qcm/filtered-questions-data';
import { OptionDto } from 'src/app/share/dtos/qcm/option-dto';
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
  @Input() qcms$!: Observable<QcmDto[]>;
  allQuestions$: Observable<QuestionDto[]>;
  qcmForm!: FormGroup;
  subscriptions: Subscription[] = [];
  latestQuestions: QuestionDto[] = [];
  qcmNameExists$!: Observable<boolean>;
  optionsTechnoNiveau: OptionDto[] = [];
  niveauxPourTechnologieChoisie: { label: string, value: string }[] = [];
  optionsNiveauArray: any[] = [];

  categories = Object.values(Categorie).map(cat => ({ name: cat }));
  technologies = Object.values(Technologie).map(tech => ({ name: tech }));
  niveaux = Object.values(Niveau).map(niv => ({ name: niv }));
  nbreQuestion = Array.from({ length: 100 }, (_, i) => ({ name: i.toString() }));

  constructor(private questionService: QuestionService,
              private formBuilder: FormBuilder,
              private qcmService: QcmService) {
    const cachedQuestions = localStorage.getItem('questions_cache');
    this.allQuestions$ = cachedQuestions ? of(JSON.parse(cachedQuestions)) : this.questionService.questions$;
   }

  ngOnInit(): void {
    this.optionsTechnoNiveau = this.createTechnoNiveauOptions();
    this.optionsNiveauArray.push([...this.optionsTechnoNiveau]);
    if (!localStorage.getItem('questions_cache')) {
      this.subscriptions.push(this.questionService.loadAllQuestions().subscribe());
    }

    this.qcmForm = this.formBuilder.group({
      nom: ['', Validators.required],
      rows: this.formBuilder.array([this.createRow()])
    });

    this.rows.controls.forEach((control: AbstractControl, index: number) => {
      this.ecouterChangements(index);
    });

    this.allQuestions$.subscribe(questions => {
      this.latestQuestions = questions;
    });

    this.qcmNameExists$ = this.qcmForm.get('nom')!.valueChanges.pipe(
      switchMap(name =>
        this.qcms$.pipe(
          map(qcms => qcms.some(qcm => qcm.nom === name))
        )
      ),
      startWith(false)
    );
    
  }

  createTechnoNiveauOptions(): OptionDto[] {
    const options: OptionDto[] = [];
    for (let tech of Object.values(Technologie)) {
      options.push({
        technologie: tech,
        niveaux: Object.values(Niveau).map(niv => ({ label: niv, value: niv }))
      });
    }
    return options;
  }

  onTechnologieChange(event: any) {
    const technologieObj = event.value;
    if (technologieObj && technologieObj.niveaux) {
      this.niveauxPourTechnologieChoisie = technologieObj.niveaux;
    } else {
        this.niveauxPourTechnologieChoisie = [];
    }
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
      return this.qcmForm.get('rows') as FormArray;
  }

  createRow(): FormGroup {
    return this.formBuilder.group({
      categorie: [''],
      technologie: ['', Validators.required],
      niveau: ['', Validators.required],
      nbreQuestion: ['', Validators.required]
    });
  }

  addRow(): void {
    const rows = this.qcmForm.get('rows') as FormArray;
    const lastGroup = rows.at(rows.length - 1) as FormGroup;
    const technologieObj  = lastGroup.get('technologie')?.value;
    const niveauValue = lastGroup.get('niveau')?.value;
    const nbreQuestion = lastGroup.get('nbreQuestion')?.value

    if(technologieObj && niveauValue && nbreQuestion ) {
      const technologieValue = technologieObj.technologie;

      // Clonez optionsTechnoNiveau profondément
      const clonedOptions = JSON.parse(JSON.stringify(this.optionsTechnoNiveau));

      // Trouvez la technologie dans la copie clonée et supprimez le niveau
      const techObj = clonedOptions.find((tech: any) => tech.technologie === technologieValue);
      if (techObj) {
          const index = techObj.niveaux.findIndex((niveau: any) => niveau.value === niveauValue);
          if (index > -1) {
              techObj.niveaux.splice(index, 1);
          }
      }
      // Ajoutez la copie modifiée à optionsNiveauArray
      this.optionsNiveauArray.push(clonedOptions);
        
      // Ajoutez un nouveau formulaire de groupe à la liste de lignes
      this.rows.push(this.createRow());
      this.ecouterChangements(this.rows.length - 1);
    }
  }

  getNiveauxOptions(index: number, technologie: any): any[] {
    const techOption = this.optionsNiveauArray[index]?.find((tech: any) => tech.technologie === technologie?.technologie);
    return techOption?.niveaux || [];
  }

  subRow(): void {
    const index = this.rows.length -1;
    if(index >= 1)
      this.rows.removeAt(index);
  }

  submitQcm(): void {
    if(this.qcmForm.get('rows')?.hasError('duplicateRows')) {
      console.error('Combinaison de filtre déjà existante');
      return;
    }
    const formData = this.qcmForm.value;
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

      grandTotalPoints += totalTime;
      grandTotalTime += totalpoint;

      allFilteredQuestion.push({
        questions: selectedQuestions,
        totalTime: totalTime,
        totalPoints: totalpoint
      });
    });
    const qcmData: QcmDto = {
      nom: formData.nom,
      temps: grandTotalTime,
      point: grandTotalPoints
    }
    this.qcmService.addQcm(qcmData).subscribe( (response: QcmDto) => {
       const newQcmId = response.id;
          if (typeof newQcmId === 'number'){
            const allQuestionIds: number[] = [];
            allFilteredQuestion.forEach(data => {
              data.questions.forEach(question => {
                if (question.id){
                  allQuestionIds.push(question.id);
                }
              });
          });
          this.qcmService.addQuestionToQcm(newQcmId, allQuestionIds).subscribe((resp: any) => {
            console.log('Questions ajoutées avec sucés: ', resp);
          },
          (error: any) => {
            console.error('Erreur lors de l\'ajout des questions:', error)
          })
          }
       console.log('Données du QCM envoyées avec succès!', response);
     },
     error => {
       console.error('Erreur lors de l\'envoi des données du QCM', error);
      }
    )
  }

  shuffleArray(array: any[]): any[] {
    for(let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
