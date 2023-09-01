import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, shareReplay, tap } from 'rxjs';
import { EntretienDto } from 'src/app/share/dtos/entretien/entretien-dto';
import { ScoreDto } from 'src/app/share/dtos/entretien/score-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionTriDto } from 'src/app/share/dtos/question/question-tri-dto';
import { CorrectAnswerDto } from 'src/app/share/dtos/reponse/correct-answer-dto';
import { ReponseCandidatQuestionDto } from 'src/app/share/dtos/reponse/reponse-candidat-question-dto';
import { ReponseQcmDto } from 'src/app/share/dtos/reponse/reponse-qcm-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EntretienService {
  private entretienSubject = new BehaviorSubject<EntretienDto | null>(null);
  entretien$ = this.entretienSubject.asObservable().pipe(
    shareReplay(1)
  );
  private allEntretienSubject = new BehaviorSubject<EntretienDto[]>([]);
  allEntretien$ = this.allEntretienSubject.asObservable().pipe(
    shareReplay(1)
  );
  private entretien: EntretienDto[] = [];

  constructor(private httpClient: HttpClient) {
    this.loadInitialData();
    this.allEntretien$.subscribe(data => {
      this.entretien = data
    })
  }

  private loadInitialData() {
    const personneStr = sessionStorage.getItem('personne');
    if (personneStr) {
      const personne = JSON.parse(personneStr);
      const personneId = personne.entretienId;
      if (personneId) {
        this.getEntretienById(Number(personneId)).subscribe(
          (data) => {
            this.entretienSubject.next(data);
            sessionStorage.setItem('entretien', JSON.stringify(data));
          },
          (error) => {
            console.error('Failed to fetch data', error);
          }
        );
      }
    }
  }

  getEntretienById(id: number) {
    return this.httpClient.get<EntretienDto>(`${environment.getEntretien}${id}`);
  }

  createEntretien(data: EntretienDto): Observable<EntretienDto> {
    return this.httpClient.post<EntretienDto>(environment.addEntretien, data)
  }

  getAllEntretien(): Observable<EntretienDto[]> {
    const sortEntretien = (entretiens: EntretienDto[]) =>
      entretiens.sort((a, b) => (b.id ?? 0 ) - (a.id ?? 0));

      return this.httpClient.get<EntretienDto[]>(environment.getAllEntretien).pipe(
        map(entretien => sortEntretien(entretien)),
        tap(sortedEntretiens => {
          this.allEntretienSubject.next(sortedEntretiens)
        })
      );
  }

  transformQuestions(qcmQuestion: QuestionDto[]): QuestionTriDto[] {
    const groupedByTech: { [key: string]: QuestionDto[] } = {};

    if(qcmQuestion) {
      qcmQuestion.forEach((question) => {
        const tech = question.technologie ?? "Autre";
        if (!groupedByTech[tech]) {
          groupedByTech[tech] = [];
        }
        groupedByTech[tech].push(question);
      });
    }

    const result: QuestionTriDto[] = Object.keys(groupedByTech).map((tech) => {
      return {
        technologie: tech,
        questions: groupedByTech[tech]
      };
    });
    return result;
  }

  transformReponses = (reponseCandidat: ReponseQcmDto[], qcmQuestion: QuestionDto[]): ReponseCandidatQuestionDto[] => {
    const groupedAnswers: { [key: number]: { answers: number[], technologie?: string } } = {};

    if (reponseCandidat) {
      reponseCandidat.forEach((reponse) => {
        const correspondingQuestion = qcmQuestion.find(q => {
          return q.reponses?.some(p => p.id === reponse.idProposition);
        });

        const questionId = correspondingQuestion?.id;
        const questionTechnologie = correspondingQuestion?.technologie;

        if (questionId !== undefined) {
          if (!groupedAnswers[questionId]) {
            groupedAnswers[questionId] = { answers: [], technologie: questionTechnologie };
          }
          groupedAnswers[questionId].answers.push(reponse.idProposition);
        }
      });
    }

    const result: ReponseCandidatQuestionDto[] = Object.keys(groupedAnswers).map((key) => {
      return {
        question_id: Number(key),
        candidateAnswer: groupedAnswers[Number(key)].answers,
        technologie: groupedAnswers[Number(key)].technologie
      };
    });

    return result;
  };

    isCandidateAnswer(transformedReponses: ReponseCandidatQuestionDto[], questionId: number, answerId: number): boolean {
      const foundQuestion = transformedReponses.find(q => q.question_id === questionId);
      return foundQuestion ? foundQuestion.candidateAnswer.includes(answerId) : false;
    }

    transformToCorrectAnswerDto = (questionArray: QuestionTriDto[]): CorrectAnswerDto[] => {
      const result: CorrectAnswerDto[] = [];

      questionArray.forEach((questionGroup) => {
        questionGroup.questions?.forEach((question) => {
          const correctAnswer: number[] = [];
          const incorrectAnswer: number[] = [];


          question.reponses?.forEach((reponse) => {
            if(reponse.id) {
              if (reponse.correct) {
                correctAnswer.push(reponse.id);
              } else {
                incorrectAnswer.push(reponse.id);
              }
            }
          });


          result.push({
            question_id: question.id!,
            technologie: question.technologie!,
            correctAnswer,
            incorrectAnswer,
            point: question.point
          });
        });
      });

      return result;
    };

    calculateScores(
      transformedResponses: ReponseCandidatQuestionDto[],
      correctAnswers: CorrectAnswerDto[]
    ): ScoreDto[] {
      const scores: { [key: string]: ScoreDto } = {};
      correctAnswers.forEach((correctAnswer) => {
        const { technologie, point } = correctAnswer;
        if (!scores[technologie]) {
          scores[technologie] = {
            technologie,
            scoreTotal: 0,
            scoreCandidat: 0,
          };
        }
        scores[technologie].scoreTotal += point;
      });
      transformedResponses.forEach((response) => {
        const { question_id, candidateAnswer, technologie } = response;
        const correctAnswer = correctAnswers.find(
          (answer) => answer.question_id === question_id
        );

        if (correctAnswer) {
          if (correctAnswer.correctAnswer.length === candidateAnswer.length) {
            const allCorrect = candidateAnswer.every((ans: number) =>
              correctAnswer.correctAnswer.includes(ans)
            );
            const noneIncorrect = candidateAnswer.every(
              (ans: number) => !correctAnswer.incorrectAnswer.includes(ans)
            );
            if (allCorrect && noneIncorrect) {
              scores[correctAnswer.technologie].scoreCandidat += correctAnswer.point;
            }
          }
        }
      });

      return Object.values(scores);
    }
}
