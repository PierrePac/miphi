import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { QcmComponent } from './component/qcm/qcm.component';
import { ViewQcmComponent } from './component/view-qcm/view-qcm.component';
import { CandidatsComponent } from './component/candidats/candidats.component';
import { QuestionComponent } from './component/question/question.component';
import { ShareModule } from '../share/share.module';
import { AddAdminComponent } from './component/add-admin/add-admin.component';
import { CreateQcmComponent } from './component/create-qcm/create-qcm.component';
import { CreateCandidatComponent } from './component/create-candidat/create-candidat.component';
import { ViewResultComponent } from './component/view-result/view-result.component';


@NgModule({
  declarations: [
    QcmComponent,
    ViewQcmComponent,
    CandidatsComponent,
    QuestionComponent,
    AddAdminComponent,
    CreateQcmComponent,
    CreateCandidatComponent,
    ViewResultComponent
  ],
  imports: [
    AdminRoutingModule,
    CommonModule,
    ShareModule,
  ]
})
export class AdminModule { }
