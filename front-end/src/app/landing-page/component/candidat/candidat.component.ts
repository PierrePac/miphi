import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthenticationService } from 'src/app/core/services/authentication/authentication.service';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';
import { QuestionQcmDto } from 'src/app/share/dtos/question/question-qcm-dto';
import { ReponseCandidatDto } from 'src/app/share/dtos/reponse/reponse-candidat-dto';

@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrls: ['./candidat.component.scss']
})
export class CandidatComponent implements OnInit {
  questionsList!: QuestionDto[];
  filteredQuestion!: QuestionDto[];
  questionQcm!: QuestionQcmDto[];
  activeSection!: string;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private questionService: QuestionService,
              private qcmService: QcmService,
              private authService: AuthenticationService,) {
                // Obtention de la section active depuis les queryParams
    this.activeSection = this.route.snapshot.queryParamMap.get('section') ?? 'qcm';
  }

  ngOnInit(): void {
    // Récupère les informations de la personne depuis sessionStorage
    const personneJSON = sessionStorage.getItem('personne');
    const personne = personneJSON ? JSON.parse(personneJSON) : null;
    // Récupération de toutes les questions (avec toutes les réponses à false)
    this.questionService.loadAllQuestionsWr().subscribe(
      (data: QuestionDto[]) => {
        this.questionsList = data;
        // Récupération des questions QCm associées à un entretien
        this.qcmService.getQcmQuestionByEntretien(personne.entretienId).subscribe(
          (data: QuestionQcmDto[]) => {
            this.questionQcm = data;
            this.loadQuestions()
            // Récupération des réponses stockées
            let storedAnswers: ReponseCandidatDto[] = JSON.parse(sessionStorage.getItem('candidatAnswers') ?? '[]');
            // Si le nombre de réponses stockées est égal au nombre de questions QCM et que la section active est 'qcm'
            if(storedAnswers.length == this.questionQcm.length && this.activeSection === 'qcm') {
              this.towardCode(); // Redirige vers le code
            } else if (this.questionQcm.length > 0 &&  this.activeSection === 'qcm') {
              this.towardQcm(); // Redirige vers le QCM
            }
          }
        )
      }
    )
  }

  loadQuestions() {
    of(this.questionsList).pipe(
      map(allQuestions => {
        // Filtre les questions
        return allQuestions.filter(q => {
          return this.questionQcm.some(qcm => qcm.idQuestion === q.id);
        });
      }),
      map(filteredQuestions => {
        // Trie les questions filtrées selon leur ordre
        return filteredQuestions.sort((a, b) => {
          const ordreA = this.questionQcm.find(qcm => qcm.idQuestion === a.id)?.ordre;
          const ordreB = this.questionQcm.find(qcm => qcm.idQuestion === b.id)?.ordre;

          if (ordreA === undefined || ordreB === undefined) {
            return 0; // ou autre logique de tri pour les valeurs non définies
          }

          return ordreA - ordreB;
        });
      })
    ).subscribe(sortedAndFilteredQuestions => {
      this.filteredQuestion = sortedAndFilteredQuestions;
      sessionStorage.setItem('question_qcm', JSON.stringify(this.filteredQuestion));
    });
  }

  towardQcm(){
    this.router.navigate(['/entretien/qcm']);
  }

  towardCode(){
    this.router.navigate(['/entretien/sandbox']);
  }

  // Fin du test : nettoie les données de session et locales puis redirige vers la page d'accueil
  endTest(){
    sessionStorage.clear();
    window.localStorage.clear();
    this.authService.isLoggedIn.next(false);
    this.router.navigateByUrl('');
  }
}
