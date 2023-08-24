import { Component, OnInit } from '@angular/core';
import { TimerService } from 'src/app/core/services/timer/timer.service';
import { CandidatDto } from 'src/app/share/dtos/candidat/candidat-dto';
import { FullEntretienDto } from 'src/app/share/dtos/entretien/full-entretien-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { ReponseCandidatDto } from 'src/app/share/dtos/reponse/reponse-candidat-dto';

@Component({
  selector: 'app-qcm',
  templateUrl: './qcm.component.html',
  styleUrls: ['./qcm.component.scss']
})
export class QcmComponent implements OnInit {
  public sessionData: { entretien: FullEntretienDto, filteredQuestions: QuestionDto[] } | null = { entretien: {}, filteredQuestions: [] };
  public candidat!: CandidatDto;
  currentIndex: number = 0;
  selectedAnswers: any[] = [];
  showButton: boolean = true;

  constructor(private timerService: TimerService) { }

  ngOnInit(): void {
    const sessionDataString = sessionStorage.getItem('entretien');
    if(sessionDataString) {
      this.sessionData = JSON.parse(sessionDataString);
      const temps = this.sessionData?.entretien?.qcm?.temps;
      if (temps !== undefined) {
        this.timerService.initializeTimer(temps);
      }
    }
    let storedAnswers: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') || '[]');
    storedAnswers.forEach(answer => {
      if(answer.reponse_candidat_id.question_id !== undefined && answer.reponse_id.id !== undefined) {
        this.selectedAnswers[answer.reponse_candidat_id.question_id] = answer.reponse_id.id;
      }
    })

    const candidatDetails = sessionStorage.getItem('personne');
    if(candidatDetails){
      this.candidat = JSON.parse(candidatDetails);
      const candidatId = this.candidat.id;
    }
  }

  nextQuestion() {
    if (this.currentIndex < (this.sessionData?.filteredQuestions?.length || 0) - 1) {
      this.currentIndex++;
    }
    const currentQuestionId = this.sessionData?.filteredQuestions[this.currentIndex]?.id;
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
    const storedAnswer: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') || '[]');
    const answer = storedAnswer.find(a => a.reponse_candidat_id.question_id ==questionId);
    return answer?.reponse_id.id;
  }

  previousQuestion() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  validateAnswer() {
    if (!this.sessionData) return;
    const currentQuestion = this.sessionData.filteredQuestions[this.currentIndex];
    if (!currentQuestion) return;
    const currentQuestionId = currentQuestion.id;
    if(currentQuestionId === undefined) return;
    const currentAnswerId = this.selectedAnswers[currentQuestionId];
    console.log(currentAnswerId)
    if(currentAnswerId === undefined) return;
    if (currentAnswerId === undefined || currentQuestionId === undefined) return;
    const currentCandidatId = this.candidat.id;
    if(currentCandidatId === undefined) return;

    const reponse: ReponseCandidatDto = {
      reponse_candidat_id: {
        candidat_id: currentCandidatId,  // Remplacez par votre ID de candidat
        question_id: currentQuestionId,
      },
      reponse_id: {
        id: currentAnswerId,
      }
    };

    let storedAnswers: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') || '[]');
    const existingAnswerIndex = storedAnswers.findIndex(storedAnswer => storedAnswer.reponse_candidat_id.question_id === currentQuestionId);
    if(existingAnswerIndex > -1) {
      storedAnswers[existingAnswerIndex] = reponse;
    } else {
      storedAnswers.push(reponse);
    }
    sessionStorage.setItem('candidatAnswers', JSON.stringify(storedAnswers));
    this.nextQuestion();
  }

  ValidateQcm() {
    console.log(sessionStorage.getItem('candidatAnswers'));
    console.log('prout')
  }

}