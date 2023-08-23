import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { QcmComponent } from './component/qcm/qcm.component';
import { RoleGuard } from '../core/guards/role.guard';
import { SandboxComponent } from './component/sandbox/sandbox.component';

const routes: Routes = [
  { path: 'qcm', component: QcmComponent, canActivate: [RoleGuard], data: { expectedRole: 'CANDIDAT' } },
  { path: 'sandbox', component: SandboxComponent, canActivate: [RoleGuard], data: { expectedRole: 'CANDIDAT' } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EntretienRoutingModule { }
