import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TimerService {
  remainingTime = new BehaviorSubject<number>(0);
  private intervalId?: number;

  // Initialise et démarre le compte à rebours basé sur une valeur donnée (temps).
  initializeTimer(temps: number): void {
    const endTime = this.getOrInitializeEndTime(temps);
    this.clearTimer();
    this.intervalId = window.setInterval(() => {
      const currentTime = new Date().getTime();
      const remainingTimeInMilliseconds = endTime - currentTime;
      const remainingTime = Math.floor(remainingTimeInMilliseconds / 1000);
      this.remainingTime.next(remainingTime);
      if(remainingTimeInMilliseconds <= 0){
        this.clearTimer();
        console.log('le temps est écoulé !');
      }
    }, 1000);
  }

  // Récupère le temps de fin depuis la session storage ou initialise un nouveau si ce n'est pas le cas.
  private getOrInitializeEndTime(temps: number): number {
    let endTime = sessionStorage.getItem('endTime');
    if(!endTime) {
      const currentTime = new Date().getTime();
      endTime = (currentTime + temps * 1000).toString();
      sessionStorage.setItem('endTime', endTime);
    }
    return Number(endTime);
  }

  // Réinitialise le compte à rebours à une valeur donnée ou à zéro par défaut.
  // Supprime également le temps de fin de la session storage.
  resetTimer(initialTime: number = 0) {
    this.remainingTime.next(initialTime);
    sessionStorage.removeItem('endTime');
    this.clearTimer();
  }

  // Arrête le compte à rebours en effaçant l'intervalle.
  private clearTimer(): void {
    if(this.intervalId !== undefined) {
      clearInterval(this.intervalId);
      this.intervalId = undefined;
    }
  }

}
