import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './component/header/header.component';
import { HttpClientModule } from '@angular/common/http';
import { httpInterceptorProviders } from './interceptor';
import { ShareModule } from '../share/share.module';





@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    ShareModule,
  ],
  providers: [
    httpInterceptorProviders
  ],
  exports: [
    HeaderComponent
  ]
})
export class CoreModule { }
