import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  cards = [
    { title: 'Créer un QCM', route: 'admin/create-qcm', image: 'assets/media/create_qcm.jpg' },
    { title: 'Visualiser les QCMs', route: 'admin/view-qcm', image:'assets/media/list_qcm.jpg' },
    { title: 'Visualiser les candidats', route:'admin/view-candidats', image:'assets/media/candidats.jpg' },
    { title:'Visualiser les questions', route:'admin/view-question', image:'assets/media/add_question.jpg' }
  ]

  constructor(private router: Router) {}

  ngOnInit(): void {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

}
