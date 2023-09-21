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
  // Déclaration de variables pour suivre l'état des entretiens
  private entretienSubject = new BehaviorSubject<EntretienDto | null>(null);
  entretien$ = this.entretienSubject.asObservable().pipe(shareReplay(1));
  private allEntretienSubject = new BehaviorSubject<EntretienDto[]>([]);
  allEntretien$ = this.allEntretienSubject.asObservable().pipe(shareReplay(1));

  // Variables pour stocker les scores et réponses
  private allGoodAnswer: number[] = [];
  private partiallyGoodAnswer: number[] = [];
  private noneGoodAnswer: number[] = [];

  constructor(private httpClient: HttpClient) {
    this.loadInitialData();

  }

  // Charge les données initiales depuis la session
  private loadInitialData() {
    const personneStr = sessionStorage.getItem('personne');
    if (personneStr) {
      const personne = JSON.parse(personneStr);
      const personneId = personne.entretienId;
      if (personneId) {
        this.getEntretienById(Number(personneId)).subscribe(
          (data) => { // En cas de succès, mise à jour du BehaviorSubject
            this.entretienSubject.next(data);
            sessionStorage.setItem('entretien', JSON.stringify(data));
          },
          (error) => { // Gestion d'erreur
            console.error('Failed to fetch data', error);
          }
        );
      }
    }
  }

  // Récupération d'un entretien par son ID
  getEntretienById(id: number) {
    return this.httpClient.get<EntretienDto>(`${environment.getEntretien}${id}`);
  }

  // Création d'un nouvel entretien
  createEntretien(data: EntretienDto): Observable<EntretienDto> {
    return this.httpClient.post<EntretienDto>(environment.addEntretien, data)
  }

  // Récupération de tous les entretiens
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

  // trie des questions par technologie
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
    // Initialisation d'un objet pour regrouper les réponses par question
    const groupedAnswers: { [key: number]: { answers: number[], technologie?: string } } = {};

    if (reponseCandidat) {
       // Parcours de chaque réponse du candidat et recherche de la question correspondante à la réponse du candidat dans qcmQuestion
      reponseCandidat.forEach((reponse) => {
        const correspondingQuestion = qcmQuestion.find(q => {
          return q.reponses?.some(p => p.id === reponse.idProposition);
        });

        // Extraction de l'ID et de la technologie de la question correspondante
        const questionId = correspondingQuestion?.id;
        const questionTechnologie = correspondingQuestion?.technologie;

        // Si l'ID de la question est trouvé (n'est pas undefined)
        // Initialisation de la clé correspondante dans groupedAnswers si elle n'existe pas déjà
        if (questionId !== undefined) {
          if (!groupedAnswers[questionId]) {
            groupedAnswers[questionId] = { answers: [], technologie: questionTechnologie };
          }
          // Ajout de l'ID de la réponse du candidat à la liste des réponses pour cette question
          groupedAnswers[questionId].answers.push(reponse.idProposition);
        }
      });
    }

    // Transformation de l'objet groupedAnswers en un tableau de type ReponseCandidatQuestionDto
    const result: ReponseCandidatQuestionDto[] = Object.keys(groupedAnswers).map((key) => {
      return {
        question_id: Number(key),
        candidateAnswer: groupedAnswers[Number(key)].answers,
        technologie: groupedAnswers[Number(key)].technologie
      };
    });

    return result;
  };

  // Vérifie si la réponse d'un candidat à une question donnée est correcte ou non
  isCandidateAnswer(transformedReponses: ReponseCandidatQuestionDto[], questionId: number, answerId: number): boolean {
    // Recherche la question correspondant à questionId dans le tableau des réponses transformées
    const foundQuestion = transformedReponses.find(q => q.question_id === questionId);
    // Vérifie si la réponse trouvée contient l'ID de la réponse du candidat
    // Retourne true si c'est le cas, sinon false
    return foundQuestion ? foundQuestion.candidateAnswer.includes(answerId) : false;
  }

  // trie les questions correct et incorrect 
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
          niveau: question.niveau!,
          correctAnswer,
          incorrectAnswer,
          point: question.point
        });
      });
    });
    return result;
  };

    calculateScores(transformedResponses: ReponseCandidatQuestionDto[], correctAnswers: CorrectAnswerDto[]): ScoreDto[] {
      const scores: { [key: string]: ScoreDto } = {};
      correctAnswers.forEach((correctAnswer) => {
        const { technologie, point, niveau } = correctAnswer;
        if (!scores[technologie]) {
          scores[technologie] = {
            technologie,
            niveaux: [],
          };
        }
        const niveauScore = scores[technologie].niveaux.find(n => n.niveau === niveau) || {
          niveau,
          scoreTotal: 0,
          scoreCandidat:0,
        };

        niveauScore.scoreTotal += point;
        if(!scores[technologie].niveaux.some(n => n.niveau === niveau)) {
          scores[technologie].niveaux.push(niveauScore);
        }
      });

      transformedResponses.forEach((response) => {
        const { question_id, candidateAnswer, technologie } = response;
        const correctAnswer = correctAnswers.find(
          (answer) => answer.question_id === question_id
        );

        let niveauScore;

        if (correctAnswer) {
          if(technologie && scores[technologie]) {
            niveauScore = scores[technologie].niveaux.find((n: { niveau: string; scoreTotal: number; scoreCandidat: number }) => n.niveau === correctAnswer.niveau);
            if(!niveauScore) return;
          }

          const allCorrect = candidateAnswer.every((ans: number) =>
            correctAnswer.correctAnswer.includes(ans)
          );
          const noneIncorrect = candidateAnswer.every(
            (ans: number) => !correctAnswer.incorrectAnswer.includes(ans)
          );
          const oneCorrect = candidateAnswer.some((ans: number) =>
            correctAnswer.correctAnswer.includes(ans)
          );

          if (correctAnswer.correctAnswer.length === candidateAnswer.length) {
            if (allCorrect && noneIncorrect) {
              if(niveauScore) {
                niveauScore.scoreCandidat += correctAnswer.point;
              }
              this.allGoodAnswer.push(question_id);
            } else if(oneCorrect && !allCorrect) {
              this.partiallyGoodAnswer.push(question_id);
            } else {
              this.noneGoodAnswer.push(question_id);
            }

          } else {

            if((allCorrect && noneIncorrect) || oneCorrect) {
              this.partiallyGoodAnswer.push(question_id)
            } else {
              this.noneGoodAnswer.push(question_id);
            }

          }
        }
      });
      return Object.values(scores);
    }

    getAllGoodAnswer(){
      return this.allGoodAnswer;
    }
    getPartiallyGoodAnswer(){
      return this.partiallyGoodAnswer;
    }
    getNoneGoodAnswer(){
      return this.noneGoodAnswer;
    }
}
