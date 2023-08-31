import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { FullEntretienDto } from 'src/app/share/dtos/entretien/full-entretien-dto';
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
              private timerService: TimerService)
    {
      this.router.events.subscribe(event => {
        if (event instanceof NavigationEnd) {
          this.showDiv = this.router.url === '/entretien/qcm';
        }
      });
    }

  ngOnInit(): void {
    this.subscription = this.authService.isLoggedIn.subscribe(isLoggedin => {
      this.items = this.getRoutesBasedOnRole();
      this.showMenu = isLoggedin;
      this.activeItem = this.items[0];
    })
    this.timerService.remainingTime.subscribe(time => {
      this.remainingTime = time;
    })
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
    { label: 'Gérer les candidats', routerLink: 'admin/candidats' },
    { label: 'Gérer les questions', routerLink: 'admin/question' },
    { label: 'Ajouter un admin', routerLink: 'admin/add-admin' },
    { label: 'Se déconnecter', command: (event: any) => this.logout() }
  ];

  private candidatRoutes = [
    // pour le dev
    //{ label: 'Se déconnecter', command: (event: any) => this.logout() }
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
    this.timerService.resetTimer();
    sessionStorage.clear();
    window.localStorage.clear();
    this.authService.isLoggedIn.next(false);
    this.router.navigateByUrl('');
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}

