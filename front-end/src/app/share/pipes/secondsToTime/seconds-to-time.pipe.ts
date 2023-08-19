import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'secondsToTime'
})
export class SecondsToTimePipe implements PipeTransform {

  transform(value: number): string {
    if (!Number.isInteger(value)) {
      return value.toString();
    }

    const hours = Math.floor(value / 3600);
    const minutes = Math.floor((value % 3600) / 60);
    const seconds = value % 60;

    const hoursDisplay = hours > 0 ? `${hours}h ` : '';
    const minutesDisplay = minutes > 0 || hours > 0 ? `${minutes}min ` : '';
    const secondsDisplay = `${seconds}sec`;

    return hoursDisplay + minutesDisplay + secondsDisplay;
  }

}
