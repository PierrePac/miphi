import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { QuestionService } from 'src/app/core/services/question/question.service';
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
    { title: 'Créer un QCM', route: 'admin/create-qcm', image: 'assets/media/create_qcm.jpg' },
    { title: 'Visualiser les QCMs', route: 'admin/view-qcm', image:'assets/media/list_qcm.jpg' },
    { title: 'Visualiser les candidats', route:'admin/view-candidats', image:'assets/media/candidats.jpg' },
    { title:'Visualiser les questions', route:'admin/view-question', image:'assets/media/add_question.jpg' },
    { title:'Ajouter un admin', route:'admin/add-admin', image:'assets/media/add_admin.jpg' }
  ]

  constructor(private questionService: QuestionService,
              private router: Router) { }

  ngOnInit(): void {
    this.questionService.loadAllQuestions();
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

}
