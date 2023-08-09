import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Personne } from 'src/app/share/models/personne/personne.model';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
      const personneData = sessionStorage.getItem('personne');
      if (personneData) {
        const personne = JSON.parse(personneData) as Personne;
        if (route.data['expectedRole'] === personne.role) {
          return true;
        }
      }
      this.router.navigate(['']);
      return false;
  }
};
