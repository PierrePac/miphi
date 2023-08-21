import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import {ToastModule} from 'primeng/toast';
import {InputNumberModule} from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { FieldsetModule } from 'primeng/fieldset';
import { CheckboxModule } from 'primeng/checkbox';
import { ShareQuestionComponent } from './component/question/share-question/share-question.component';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormatEnumPipe } from './pipes/FormatEnum/format-enum-pipe';
import { DividerModule } from 'primeng/divider';
import { TabMenuModule } from 'primeng/tabmenu';
import { SidebarModule } from 'primeng/sidebar';
import { OrderListModule } from 'primeng/orderlist';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { SecondsToTimePipe } from './pipes/secondsToTime/seconds-to-time.pipe';
import { InputSwitchModule } from 'primeng/inputswitch';

@NgModule({
  declarations: [
    ShareQuestionComponent,
    FormatEnumPipe,
    SecondsToTimePipe,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CardModule,
    ButtonModule,
    ToastModule,
    InputNumberModule,
    InputTextareaModule,
    DropdownModule,
    InputTextModule,
    FieldsetModule,
    CheckboxModule,
    SelectButtonModule,
    DividerModule,
    TabMenuModule,
    SidebarModule,
    OrderListModule,
    DragDropModule,
    InputSwitchModule,
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CardModule,
    ButtonModule,
    ToastModule,
    InputNumberModule,
    InputTextareaModule,
    DropdownModule,
    InputTextModule,
    FieldsetModule,
    CheckboxModule,
    ShareQuestionComponent,
    SelectButtonModule,
    DividerModule,
    TabMenuModule,
    SidebarModule,
    OrderListModule,
    DragDropModule,
    SecondsToTimePipe,
    InputSwitchModule,
  ]
})
export class ShareModule { }
