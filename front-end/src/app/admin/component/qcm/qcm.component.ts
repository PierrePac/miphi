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
  allQuestions$: Observable<QuestionDto[]>;
  toggleAddQcm: boolean = false;
  qcms$!: Observable<QcmDto[]>;


  constructor(private questionService: QuestionService,
              private qcmService: QcmService ) {
    this.allQuestions$ = this.questionService.questions$;
  }
  
  ngOnInit(): void {
    this.qcms$ = this.qcmService.qcms$;
    this.qcmService.getQcms();
    console.log(this.qcms$);
  }
}
