import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { EntretienService } from 'src/app/core/services/entretien/entretien.service';
import { PersonneService } from 'src/app/core/services/personne/personne.service';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { ReponseService } from 'src/app/core/services/reponse/reponse.service';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { ScoreDto } from 'src/app/share/dtos/entretien/score-dto';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionTriDto } from 'src/app/share/dtos/question/question-tri-dto';
import { CorrectAnswerDto } from 'src/app/share/dtos/reponse/correct-answer-dto';
import { ReponseCandidatQuestionDto } from 'src/app/share/dtos/reponse/reponse-candidat-question-dto';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';

@Component({
  selector: 'app-view-result',
  templateUrl: './view-result.component.html',
  styleUrls: ['./view-result.component.scss']
})
export class ViewResultComponent implements OnInit{

  private qcmCandidatSubject$ = new BehaviorSubject<QcmDto | null>(null);
  public qcmCandidat$ = this.qcmCandidatSubject$.asObservable();

  private qcmQuestionSubject$ = new BehaviorSubject<QuestionDto[] | null>(null);
  public qcmQuestion$ = this.qcmQuestionSubject$.asObservable();

  private reponseCandidatSubject$ = new BehaviorSubject<ReponseQcmDto[] | null>(null);
  public reponseCandidat$ = this.reponseCandidatSubject$.asObservable();

  private allCandidatsSubject$: BehaviorSubject<CandidatDto[]> = new BehaviorSubject<CandidatDto[]>([]);
  public allCandidats$ = this.allCandidatsSubject$.asObservable();

  candidats: CandidatDto[] = [];
  qcmCandidat!: QcmDto;
  qcmQuestion!: QuestionDto[];
  reponseCandidat!: ReponseQcmDto[];
  transformedQuestions!: QuestionTriDto[];
  filteredQuestions!: QuestionTriDto[];
  public transformedReponses: ReponseCandidatQuestionDto[] = [];
  scores!: ScoreDto[];
  globalSuccessRate: number = 0;
  globalRating!: number;
  allGoodAnswer: number[] = [];
  partiallyGoodAnswer: number[] = [];
  noneGoodAnswer: number[] = [];

  constructor(private qcmService: QcmService,
              private entretienService: EntretienService,
              private personneService: PersonneService,
              private questionService: QuestionService,
              private reponseService: ReponseService) {}

  ngOnInit(): void {
    this.fetchAllCandidats();
    console.log(this.allCandidats$)
  }

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
      if(qcmQuestion){
        this.qcmQuestion = qcmQuestion;
        this.transformedQuestions = this.entretienService.transformQuestions(this.qcmQuestion);
        this.filteredQuestions = this.transformedQuestions;
        this.allGoodAnswer = this.entretienService.getAllGoodAnswer();
        this.partiallyGoodAnswer = this.entretienService.getPartiallyGoodAnswer();
        this.noneGoodAnswer = this.entretienService.getNoneGoodAnswer();
        this.transformedReponses = this.entretienService.transformReponses(this.reponseCandidat, this.qcmQuestion);
        const transformedData: CorrectAnswerDto[] = this.entretienService.transformToCorrectAnswerDto(this.transformedQuestions);
        this.scores= this.entretienService.calculateScores(this.transformedReponses, transformedData);
        console.log('scores', this.scores)
        let totalpoints = 0;
        let totalCandidatePoints = 0;

        this.scores.forEach(score => {
          score.niveaux.forEach(niveau => {
            totalpoints += niveau.scoreTotal;
            totalCandidatePoints += niveau.scoreCandidat;
          })
        });
        if (totalpoints != 0) {
          this.globalSuccessRate = (totalCandidatePoints / totalpoints) * 100
        }
        this.updateGlobalRating();
      }

    });

    this.reponseCandidat$.subscribe(reponseCandidat => {
      if(reponseCandidat)
        this.reponseCandidat = reponseCandidat;
    });
  }

  updateGlobalRating() {
    this.globalRating = Math.round((this.globalSuccessRate / 100) * 5);
  }

  isCandidateAnswer(questionId: number, answerId: number): boolean {
    return this.entretienService.isCandidateAnswer(this.transformedReponses, questionId, answerId);
  }

  isBorderBlue(questionId: any, reponseId: any): boolean {
    if (questionId !== undefined && reponseId !== undefined) {
      return this.isCandidateAnswer(questionId, reponseId);
    }
    return false;
  }

  filterQuestion(filter: number[]){
    this.filteredQuestions = this.transformedQuestions.map(techObj => {
      return {
        ...techObj,
        questions: techObj.questions?.filter(q => q.id !== undefined && filter.includes(q.id))
      };
    });
  }

  resetFilter(){
    return this.filteredQuestions = this.transformedQuestions;
  }

}
