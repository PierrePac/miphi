import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TimerService {
  remainingTime = new BehaviorSubject<number>(0);
  private intervalId?: number;

  constructor() { }

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

  private getOrInitializeEndTime(temps: number): number {
    let endTime = sessionStorage.getItem('endTime');
    if(!endTime) {
      const currentTime = new Date().getTime();
      endTime = (currentTime + temps * 1000).toString();
      sessionStorage.setItem('endTime', endTime);
    }
    return Number(endTime);
  }

  resetTimer(initialTime: number = 0) {
    this.remainingTime.next(initialTime);
    sessionStorage.removeItem('endTime');
    this.clearTimer();
  }


  private clearTimer(): void {
    if(this.intervalId !== undefined) {
      clearInterval(this.intervalId);
      this.intervalId = undefined;
    }
  }

}
