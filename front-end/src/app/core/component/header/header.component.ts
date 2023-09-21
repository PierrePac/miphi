import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { TimerService } from '../../services/timer/timer.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy{
  activeItem: any | undefined;
  items!: any[];
  private subscription!: Subscription
  showMenu: boolean = false;
  remainingTime!: number;
  public showDiv: boolean = false;

  constructor(private router: Router,
              private authService: AuthenticationService,
              private timerService: TimerService) {
      // Écoute les changements de route pour afficher/masquer un div
      this.router.events.subscribe(event => {
        if (event instanceof NavigationEnd) {
          this.showDiv = this.router.url === '/entretien/qcm';
        }
      });
    }

  ngOnInit(): void {
    // Souscription pour savoir si l'utilisateur est connecté ou non
    this.subscription = this.authService.isLoggedIn.subscribe(isLoggedin => {
      this.items = this.getRoutesBasedOnRole();
      this.showMenu = isLoggedin;
      this.activeItem = this.items[0];
    })

    // Souscription pour suivre le temps restant
    this.timerService.remainingTime.subscribe(time => {
      this.remainingTime = time;
    })
  }

  // Vérifie si l'utilisateur est un admin
  get isAdmin(): boolean {
    const personne = JSON.parse(sessionStorage.getItem("personne") || '{}');
    return personne.role === 'ADMIN';
  }

  // Vérifie si l'utilisateur est un candidat
  get isCandidat(): boolean {
    const personne = JSON.parse(sessionStorage.getItem("personne") || '{}');
    return personne.role === 'CANDIDAT';
  }

  // Routes disponibles pour un admin
  private adminRoutes = [
    { label: 'Gérer les Sandbox', routerLink: 'admin/sandbox' },
    { label: 'Gérer les QCM', routerLink: 'admin/qcm' },
    { label: 'Gérer les candidats', routerLink: 'admin/candidats' },
    { label: 'Gérer les questions', routerLink: 'admin/question' },
    { label: 'Ajouter un admin', routerLink: 'admin/add-admin' },
    { label: 'Se déconnecter', command: (event: any) => this.logout() }
  ];

  // Routes disponibles pour un candidat
  private candidatRoutes = [
    // pour le dev
    //{ label: 'Se déconnecter', command: (event: any) => this.logout() }
  ];

  // Renvoie les routes en fonction du rôle de l'utilisateur
  getRoutesBasedOnRole(): any[] {
    if (this.isAdmin) {
      return this.adminRoutes;
    } else if (this.isCandidat) {
      return this.candidatRoutes;
    } else {
      return []; // pour un rôle non reconnu ou un utilisateur non connecté
    }
  }

  // Fonction de déconnexion
  logout() {
    this.timerService.resetTimer();
    sessionStorage.clear();
    window.localStorage.clear();
    this.authService.isLoggedIn.next(false);
    this.router.navigateByUrl('');
  }

  // Cleanup : désinscription lors de la destruction du composant
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}

