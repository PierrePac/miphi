import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { CreateQcmComponent } from './component/create-qcm/create-qcm.component';
import { ViewQcmComponent } from './component/view-qcm/view-qcm.component';
import { ViewCandidatsComponent } from './component/view-candidats/view-candidats.component';
import { ViewQuestionComponent } from './component/view-question/view-question.component';
import { ShareModule } from '../share/share.module';
import { AddAdminComponent } from './component/add-admin/add-admin.component';


@NgModule({
  declarations: [
    CreateQcmComponent,
    ViewQcmComponent,
    ViewCandidatsComponent,
    ViewQuestionComponent,
    AddAdminComponent
  ],
  imports: [
    AdminRoutingModule,
    CommonModule,
    ShareModule
  ]
})
export class AdminModule { }
