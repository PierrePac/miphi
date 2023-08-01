import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingPageComponent } from './component/landing-page/landing-page.component';
import { RouterModule, Routes } from '@angular/router';
import { ShareModule } from '../share/share.module';
import { AdminComponent } from './component/admin/admin.component';
import { CandidatComponent } from './component/candidat/candidat.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'candidat', component: CandidatComponent }
]

@NgModule({
  declarations: [
    LandingPageComponent,
    AdminComponent,
    CandidatComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ShareModule,
  ]
})
export class LandingPageModule { }
