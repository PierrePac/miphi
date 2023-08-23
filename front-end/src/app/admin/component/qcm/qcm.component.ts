import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { QcmService } from 'src/app/core/services/qcm/qcm.service';
import { QuestionService } from 'src/app/core/services/question/question.service';
import { QcmDto } from 'src/app/share/dtos/qcm/qcm-dto';
import { QuestionDto } from 'src/app/share/dtos/question/question-dto';


@Component({
  selector: 'app-qcm',
  templateUrl: './qcm.component.html',
  styleUrls: ['./qcm.component.scss']
})
export class QcmComponent implements OnInit {
  toggleAddQcm: boolean = false;
  allQuestions$: Observable<QuestionDto[]>;
  allQcms$!: Observable<QcmDto[]>;


  constructor(private questionService: QuestionService,
              private qcmService: QcmService ) {
    this.allQuestions$ = this.questionService.questions$;
    this.allQcms$ = this.qcmService.qcms$;
  }

  ngOnInit(): void {
    this.qcmService.getQcms().subscribe();
  }
}
