import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-qcm',
  templateUrl: './view-qcm.component.html',
  styleUrls: ['./view-qcm.component.scss']
})
export class ViewQcmComponent implements OnInit {

  qcms: string[] = [];
  selectedQcm: string | null = null;
  sidebarVisible: boolean = true;

  ngOnInit(): void {

  }

  selectQcm(qcm: string) {
    this.selectedQcm = qcm;
  }

}
