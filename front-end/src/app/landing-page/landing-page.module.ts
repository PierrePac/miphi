import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LandingPageComponent } from './component/landing-page/landing-page.component';
import { ShareModule } from '../share/share.module';
import { AdminComponent } from './component/admin/admin.component';
import { CandidatComponent } from './component/candidat/candidat.component';
import { LandingPageRoutingModule } from './landing-page-routing.module';

@NgModule({
  declarations: [
    LandingPageComponent,
    AdminComponent,
    CandidatComponent
  ],
  imports: [
    LandingPageRoutingModule,
    CommonModule,
    ShareModule,
  ]
})
export class LandingPageModule { }
