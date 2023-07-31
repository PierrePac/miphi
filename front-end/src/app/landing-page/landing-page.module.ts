import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingPageComponent } from './component/landing-page/landing-page.component';
import { RouterModule, Routes } from '@angular/router';
import { ShareModule } from '../share/share.module';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
]

@NgModule({
  declarations: [
    LandingPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ShareModule,
  ]
})
export class LandingPageModule { }
