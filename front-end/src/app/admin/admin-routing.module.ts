import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateQcmComponent } from './component/create-qcm/create-qcm.component';
import { ViewQcmComponent } from './component/view-qcm/view-qcm.component';
import { ViewCandidatsComponent } from './component/view-candidats/view-candidats.component';
import { ViewQuestionComponent } from './component/view-question/view-question.component';
import { RoleGuard } from '../core/guards/role.guard';


const routes: Routes = [
  { path: 'create-qcm', component: CreateQcmComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'view-qcm', component: ViewQcmComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'view-candidats', component: ViewCandidatsComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'view-question', component: ViewQuestionComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
