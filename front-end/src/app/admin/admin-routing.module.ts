import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { QcmComponent } from './component/qcm/qcm.component';
import { CandidatsComponent } from './component/candidats/candidats.component';
import { QuestionComponent } from './component/question/question.component';
import { RoleGuard } from '../core/guards/role.guard';
import { AddAdminComponent } from './component/add-admin/add-admin.component';
import { SandboxComponent } from './component/sandbox/sandbox/sandbox.component';


const routes: Routes = [
  { path: 'qcm', component: QcmComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'candidats', component: CandidatsComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'question', component: QuestionComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'add-admin', component: AddAdminComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'sandbox', component: SandboxComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
