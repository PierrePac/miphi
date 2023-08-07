import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './component/landing-page/landing-page.component';
import { AdminComponent } from './component/admin/admin.component';
import { CandidatComponent } from './component/candidat/candidat.component';
import { RoleGuard } from '../core/guards/role.guard';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'admin', component: AdminComponent, canActivate: [RoleGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'candidat', component: CandidatComponent, canActivate: [RoleGuard], data: { expectedRole: 'CANDIDAT' } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LandingPageRoutingModule { }
