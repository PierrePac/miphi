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
    { title: 'Gérer les QCMs', route: 'admin/qcm', image:'assets/media/list_qcm.jpg' },
    { title: 'Gérer les candidats', route:'admin/candidats', image:'assets/media/candidats.jpg' },
    { title:'Gérer les questions', route:'admin/question', image:'assets/media/add_question.jpg' },
    { title:'Ajouter un admin', route:'admin/add-admin', image:'assets/media/add_admin.jpg' },
    { title:'Gérer les Sandbox', route:'admin/sandbox', image:'assets/media/sandbox.jpg' },
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
