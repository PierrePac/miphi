import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/landing', pathMatch: 'full' },
  { path: 'landing', loadChildren: () =>import('./landing-page/landing-page.module').then(m => m.LandingPageModule) },
  { path: 'admin', loadChildren: () =>import('./admin/admin.module').then(m => m.AdminModule) },
  { path: 'entretien', loadChildren: () =>import('./entretien/entretien.module').then(m => m.EntretienModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
