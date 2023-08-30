import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { QuestionTriDto } from 'src/app/share/dtos/question/question-tri-dto';
import { ReponseCandidatQuestionDto } from 'src/app/share/dtos/reponse/reponse-candidat-question';
import { CorrectAnswerDto } from 'src/app/share/dtos/reponse/correct-answer-dto';

@Component({
  selector: 'app-candidats',
  templateUrl: './candidats.component.html',
  styleUrls: ['./candidats.component.scss']
})
export class CandidatsComponent implements OnInit{
  @Input() mode:'view-candidats' | 'create-candidat' = 'view-candidats';

  private allCandidatsSubject$: BehaviorSubject<CandidatDto[]> = new BehaviorSubject<CandidatDto[]>([]);
  public allCandidats$ = this.allCandidatsSubject$.asObservable();

  private qcmCandidatSubject$ = new BehaviorSubject<QcmDto | null>(null);
  public qcmCandidat$ = this.qcmCandidatSubject$.asObservable();

  private qcmQuestionSubject$ = new BehaviorSubject<QuestionDto[] | null>(null);
  public qcmQuestion$ = this.qcmQuestionSubject$.asObservable();

  private reponseCandidatSubject$ = new BehaviorSubject<ReponseQcmDto[] | null>(null);
  public reponseCandidat$ = this.reponseCandidatSubject$.asObservable();

  allQcms$: Observable<QcmDto[]> = of([]);
  createCandidatForm!: FormGroup;
  entretienSwitch: boolean = false;
  selectedQcm?: QcmDto;
  idAdmin: number | null = null;
  candidats: CandidatDto[] = [];
  qcmCandidat!: QcmDto;
  qcmQuestion!: QuestionDto[];
  reponseCandidat!: ReponseQcmDto[];
  transformedQuestions!: QuestionTriDto[];
  public transformedReponses: ReponseCandidatQuestionDto[] = [];


  constructor(private qcmService: QcmService,
              private entretienService: EntretienService,
              private personneService: PersonneService,
              private questionService: QuestionService,
              private reponseService: ReponseService,
              private formBuilder: FormBuilder) {
    this.allQcms$ = this.qcmService.qcms$;
  }

  ngOnInit(): void {
    this.fetchAllCandidats();
    this.qcmService.getQcms().subscribe();
    this.createCandidatForm = this.formBuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      qcm: [''],
      sandbox: [''],
      entretien: ['']
    }, { validator: this.entretienValidation });
  }

  toggleMode() {
    if (this.mode === 'create-candidat') {
      this.mode = 'view-candidats';
    } else {
      this.mode = 'create-candidat';
    }
  }

  entretienValidation(control: AbstractControl): { [key: string]: any } | null {
    const qcm = control.get('qcm');
    const sandbox = control.get('sandbox');
    const entretien = control.get("entretien");

    if(qcm?.value && sandbox?.value) {
      return null;
    } else if (entretien?.value) {
      return null;
    } else {
      return { 'Erreur de validation': true };
    }
  }

  onSubmit() {
    const formValues = this.createCandidatForm.value;

    if (formValues.qcm && formValues.sandbox) {
      const currentDate = new Date();
      const date_end = new Date(currentDate);
      date_end.setDate(currentDate.getDate() + 30);

      const date_start = currentDate.toISOString();
      const dateEndFormatted = date_end.toISOString();

      const personneStr = sessionStorage.getItem('personne');
      if (personneStr) {
        const personne = JSON.parse(personneStr);
        this.idAdmin = personne.id;
      }

      const entretienData = {
        admin: {
          id: this.idAdmin
        },
        date_end: dateEndFormatted,
        date_start: date_start,
        qcm: { id: formValues.qcm.id },
        sandbox: { id: formValues.sandbox.id }
      };

      this.entretienService.createEntretien(entretienData).subscribe((entretien: EntretienDto) => {
        if (entretien?.id !== undefined) {
        const candidatData = {
          nom: formValues.nom.toLowerCase(),
          prenom: formValues.prenom.toLowerCase(),
          entretienId: entretien.id
        };
          this.personneService.createCandidat(candidatData).subscribe(response => {
        });
      }
      });
    } else if (formValues.entretien) {
      const candidatData = {
        nom: formValues.nom.toLowerCase(),
        prenom: formValues.prenom.toLowerCase(),
        entretienId: formValues.entretien.id
      };
      this.personneService.createCandidat(candidatData).subscribe(response => {
      });
    }
    this.createCandidatForm.reset();
  }

  EntretienToggle() {
    this.entretienSwitch = !this.entretienSwitch;
}

  // sandbox et entretien data

  sandboxOptions = [
    {id: 1, name: 'Sandbox 1', code: 'S1'},
    {id: 2, name: 'Sandbox 2', code: 'S2'},
    // ... autres options ...
  ];

  entretienOptions = [
    {id: 1, name: 'Entretien 1', code: 'E1'},
    {id: 2, name: 'Entretien 2', code: 'E2'},
    // ... autres options ...
  ];


  fetchAllCandidats() {
    this.personneService.getCandidats().subscribe(candidats => {
      this.allCandidatsSubject$.next(candidats);
      this.candidats = candidats;
    });
  }



  loadCandidatResult(candidat: CandidatDto){
    this.qcmService.getQcmByEntretien(candidat.entretienId).subscribe((data: QcmDto) => {
      this.qcmCandidatSubject$.next(data);

      if (data?.id) {
        this.questionService.getQuestionsOfQcm(data.id).subscribe((data: QuestionDto[]) => {
          this.qcmQuestionSubject$.next(data);
        });
      }
    });
    if(candidat?.id) {
      this.reponseService.getReponsesCandidat(candidat.id).subscribe((data: ReponseQcmDto[]) => {
        this.reponseCandidatSubject$.next(data)
      })
    }

    this.qcmCandidat$.subscribe(qcmCandidat => {
      if(qcmCandidat)
        this.qcmCandidat = qcmCandidat;
    });

    this.qcmQuestion$.subscribe(qcmQuestion => {
      if(qcmQuestion)
        this.qcmQuestion = qcmQuestion;
        this.transformedQuestions = this.entretienService.transformQuestions(this.qcmQuestion);
        this.transformedReponses = this.entretienService.transformReponses(this.reponseCandidat, this.qcmQuestion);
        const transformedData: CorrectAnswerDto[] = this.entretienService.transformToCorrectAnswerDto(this.transformedQuestions);
        console.log('transformedReponses', this.transformedReponses)
        console.log('transformedData', transformedData);
    });

    this.reponseCandidat$.subscribe(reponseCandidat => {
      if(reponseCandidat)
        this.reponseCandidat = reponseCandidat;
    });


  }

  isCandidateAnswer(questionId: number, answerId: number): boolean {
    return this.entretienService.isCandidateAnswer(this.transformedReponses, questionId, answerId);
  }


}
