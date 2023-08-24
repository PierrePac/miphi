import { Component, OnInit } from '@angular/core';
import { TimerService } from 'src/app/core/services/timer/timer.service';
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
  currentIndex: number = 0;
  selectedAnswers: any[] = [];

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
    console.log(sessionStorage.getItem('candidatAnswers'))
  }

  nextQuestion() {
    if (this.currentIndex < (this.sessionData?.filteredQuestions?.length || 0) - 1) {
      this.currentIndex++;
    }
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
    if(currentAnswerId === undefined) return;
    if (currentAnswerId === undefined || currentQuestionId === undefined) return;

    const reponse: ReponseCandidatDto = {
      reponse_candidat_id: {
        candidat_id: 1,  // Remplacez par votre ID de candidat
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

}
