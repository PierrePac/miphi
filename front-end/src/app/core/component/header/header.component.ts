import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy{

  activeItem: any | undefined;
  items!: any[];
  private subscription!: Subscription
  showMenu: boolean = true;

  constructor(private router: Router,
              protected authService: AuthenticationService) {
    this.router.events.subscribe(event => {
      if(event instanceof NavigationEnd) {
        this.showMenu = event.url !== '/';
      }
    });
  }

  ngOnInit(): void {
    this.subscription = this.authService.isLoggedIn.subscribe(isLoggedin => {
      console.log("isLoggedIn subscription triggered", isLoggedin);
      this.items = this.getRoutesBasedOnRole();
      this.activeItem = this.items[0];
    })
  }

  ngOnDestroy(): void {
    console.log("isLoggedIn unsubscription triggered");
    this.subscription.unsubscribe();
  }

  get isAdmin(): boolean {
    const personne = JSON.parse(sessionStorage.getItem("personne") || '{}');
    return personne.role === 'ADMIN';
  }

  get isCandidat(): boolean {
      const personne = JSON.parse(sessionStorage.getItem("personne") || '{}');
      return personne.role === 'CANDIDAT';
  }

  private adminRoutes = [
    { label: 'Gérer les QCM', routerLink: 'admin/qcm' },
    //{ label: 'Visualiser les QCMs', routerLink: 'admin/view-qcm' },
    { label: 'Gérer les candidats', routerLink: 'admin/candidats' },
    { label: 'Gérer les questions', routerLink: 'admin/question' },
    { label: 'Ajouter un admin', routerLink: 'admin/add-admin' },
    { label: 'Se déconnecter', command: (event: any) => this.logout() }
  ];

  private candidatRoutes = [

  ];

  getRoutesBasedOnRole(): any[] {
    if (this.isAdmin) {
        return this.adminRoutes;
    } else if (this.isCandidat) {
        return this.candidatRoutes;
    } else {
        return []; // pour un rôle non reconnu ou un utilisateur non connecté
    }
  }

  logout() {
    sessionStorage.clear();
    this.router.navigateByUrl('');
  }
}
