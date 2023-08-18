import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  allQuestion$: Observable<QuestionDto[]> = this.questionService.questions$;
  filteredQuestions$!: Observable<QuestionDto[]>;

  cards = [
    //{ title: 'Créer un QCM', route: 'admin/qcm', image: 'assets/media/create_qcm.jpg' },
    { title: 'Gérer les QCMs', route: 'admin/qcm', image:'assets/media/list_qcm.jpg' },
    { title: 'Gérer les candidats', route:'admin/candidats', image:'assets/media/candidats.jpg' },
    { title:'Gérer les questions', route:'admin/question', image:'assets/media/add_question.jpg' },
    { title:'Ajouter un admin', route:'admin/add-admin', image:'assets/media/add_admin.jpg' }
  ]

  constructor(private questionService: QuestionService,
              private qcmService: QcmService,
              private router: Router) { }

  ngOnInit(): void {
    this.questionService.loadAllQuestions();
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

}
