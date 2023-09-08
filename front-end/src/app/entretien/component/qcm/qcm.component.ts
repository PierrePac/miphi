import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { ReponseCandidatService } from 'src/app/core/services/reponseCandidat/reponse-candidat.service';
import { TimerService } from 'src/app/core/services/timer/timer.service';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { ReponseCandidatDto } from 'src/app/share/dtos/reponse/reponse-candidat-dto';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';

@Component({
  selector: 'app-qcm',
  templateUrl: './qcm.component.html',
  styleUrls: ['./qcm.component.scss'],
  providers: [MessageService]
})
export class QcmComponent implements OnInit {
  public candidat!: CandidatDto;
  questions!: QuestionDto[];
  qcm!: QcmDto;
  currentIndex: number = 0;
  selectedAnswers: any[] = [];
  validatedAnswer: boolean = false;
  currentCandidatId!: number;
  timerHasEnded!: number;

  constructor(private timerService: TimerService,
              private qcmService: QcmService,
              private reponseCandidatService: ReponseCandidatService,
              private router: Router,
              private messageService: MessageService) { }

  ngOnInit(): void {
    const personneJSON = sessionStorage.getItem('personne');
    const personne = personneJSON ? JSON.parse(personneJSON) : null;
    this.currentCandidatId = personne.id;
    this.qcmService.getQcmByEntretien(personne.entretienId).subscribe(
      (data: QcmDto) => {
        this.qcm = data;
        const temps = this.qcm.temps;
        if (temps !== undefined) {
          this.timerService.initializeTimer(temps);
        }
      }
    );
    const questionJson = sessionStorage.getItem('question_qcm');
    this.questions = questionJson ? JSON.parse(questionJson) : null;

    let storedAnswers: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') ?? '[]');
    this.currentIndex = storedAnswers.length;
    storedAnswers.forEach(answer => {
      if (answer.question_id !== undefined && answer.proposition_id !== undefined) {
        this.selectedAnswers[answer.question_id] = answer.proposition_id;
      }
    });

    this.timerService.remainingTime.subscribe(time => {
        this.timerHasEnded = time;
    });
  }

  show(message: string, type: string) {
    if(type === 'error')
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: message });
    if(type === 'warning')
    this.messageService.add({ severity: 'warn', summary: 'warn', detail: message });
    if(type === 'success')
    this.messageService.add({ severity: 'success', summary: 'success', detail: message });
  }

  nextQuestion() {
    if (this.currentIndex < (this.questions.length || 0) - 1) {
      this.currentIndex++;
    }
    const currentQuestionId = this.questions[this.currentIndex]?.id;
    if(currentQuestionId !== undefined) {
      const storedAnswer = this.getStoredAnswer(currentQuestionId);
      if(storedAnswer !== undefined) {
        this.selectedAnswers[currentQuestionId] = storedAnswer;
      } else {
        delete this.selectedAnswers[currentQuestionId];
      }
    }
  }

  getStoredAnswer(questionId: number): number | undefined {
    const storedAnswer: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') ?? '[]');
    const answer = storedAnswer.find(a => a.question_id ==questionId);
    this.validatedAnswer = Boolean(answer?.proposition_id);
    return answer?.proposition_id;
  }

  previousQuestion() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
    const currentQuestionId = this.questions[this.currentIndex]?.id;
    if(currentQuestionId !== undefined) {
      const storedAnswer = this.getStoredAnswer(currentQuestionId);
      if(storedAnswer !== undefined) {
        this.selectedAnswers[currentQuestionId] = storedAnswer;
      } else {
        delete this.selectedAnswers[currentQuestionId];
      }
    }
  }

  validateAnswer() {
    if (!this.questions) return;
    const currentQuestion = this.questions[this.currentIndex];
    if (!currentQuestion) return;
    const currentQuestionId = currentQuestion.id;
    if(currentQuestionId === undefined) return;
    const currentAnswerId = this.selectedAnswers[currentQuestionId];
    if(currentAnswerId === undefined) return;
    if (currentAnswerId === undefined || currentQuestionId === undefined) return;
    const currentCandidatId = this.currentCandidatId;
    if(currentCandidatId === undefined) return;

    const reponse: ReponseCandidatDto = {
        candidat_id: currentCandidatId,
        question_id: currentQuestionId,
        proposition_id: currentAnswerId,
      }

    let storedAnswers: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') ?? '[]');
    const existingAnswerIndex = storedAnswers.findIndex(storedAnswer => storedAnswer.question_id === currentQuestionId);

    if (existingAnswerIndex > -1) {
      storedAnswers[existingAnswerIndex] = reponse;
    } else {
      storedAnswers.push(reponse);
    }

    sessionStorage.setItem('candidatAnswers', JSON.stringify(storedAnswers));
    this.nextQuestion();
    }


  ValidateQcm() {
    this.validateAnswer()
    const storedAnswersJSON = sessionStorage.getItem('candidatAnswers');
    const storedAnswers: ReponseCandidatDto[] = storedAnswersJSON ? JSON.parse(storedAnswersJSON) : [];
    const transformedAnswers: ReponseQcmDto[] = [];

    storedAnswers.forEach(answer => {
      if(answer.proposition_id && Array.isArray(answer.proposition_id)) {
        answer.proposition_id.forEach(prop_id => {
          transformedAnswers.push({
            idCandidat: answer.candidat_id,
            idProposition: prop_id
          });
        });
      }
    });
    this.reponseCandidatService.postQcmAnswer(transformedAnswers).subscribe((resp: any) => {
      let message:string = resp.message
      this.show(message, 'success')
      setTimeout(() => {
        this.router.navigate(['/candidat'], { queryParams: { section: 'sandbox' } });
      }, 1500);
    });
  }

}
