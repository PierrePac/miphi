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
      // Récupère les données de 'personne' depuis sessionStorage
      const personneData = sessionStorage.getItem('personne');
      // Vérifie si les données de 'personne' existent
      if (personneData) {
        // Convertit les données JSON en objet de type Personne
        const personne = JSON.parse(personneData) as Personne;
        // Compare le rôle attendu avec le rôle de l'utilisateur
        if (route.data['expectedRole'] === personne.role) {
          return true; // Autorise la navigation vers la route
        }
      }
      // Navigue vers la route d'accueil si la condition n'est pas satisfaite
      this.router.navigate(['']);
      return false; // Bloque la navigation vers la route
  }
};
