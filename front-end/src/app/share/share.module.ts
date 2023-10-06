import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
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
import { ToggleButtonModule } from 'primeng/togglebutton';
import { TabViewModule } from 'primeng/tabview';
import { AccordionModule } from 'primeng/accordion';
import { PourcentageToRatingPipe } from './pipes/pourcentageToRating/pourcentage-to-rating.pipe';
import { RatingModule } from 'primeng/rating';
import { MultiSelectModule } from 'primeng/multiselect';
import { ConsigneFormatPipe } from './pipes/consigneFormat/consigne-format.pipe';
import { SplitterModule } from 'primeng/splitter';
import { CalendarModule } from 'primeng/calendar';
import { PaginatorModule } from 'primeng/paginator';
import { EditorModule } from 'primeng/editor';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@NgModule({
  declarations: [
    ShareQuestionComponent,
    FormatEnumPipe,
    SecondsToTimePipe,
    PourcentageToRatingPipe,
    ConsigneFormatPipe,
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
    ToggleButtonModule,
    TabViewModule,
    AccordionModule,
    RatingModule,
    MultiSelectModule,
    SplitterModule,
    CalendarModule,
    EditorModule,
    PaginatorModule,
    ConfirmDialogModule,
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
    FormatEnumPipe,
    SecondsToTimePipe,
    PourcentageToRatingPipe,
    InputSwitchModule,
    ToggleButtonModule,
    TabViewModule,
    AccordionModule,
    RatingModule,
    MultiSelectModule,
    ConsigneFormatPipe,
    SplitterModule,
    CalendarModule,
    EditorModule,
    PaginatorModule,
    ConfirmDialogModule,
  ]
})
export class ShareModule { }
