import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QcmComponent } from './component/qcm/qcm.component';
import { SandboxComponent } from './component/sandbox/sandbox.component';
import { EntretienRoutingModule } from './entretien-routing.module';



@NgModule({
  declarations: [
    QcmComponent,
    SandboxComponent
  ],
  imports: [
    CommonModule,
    EntretienRoutingModule,
  ]
})
export class EntretienModule { }
