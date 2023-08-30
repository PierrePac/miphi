import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'pourcentageToRating'
})
export class PourcentageToRatingPipe implements PipeTransform {

  transform(value: number, maxRating: number = 5): number {
    if (value >= 0 && value <= 100) {
      return Math.round((value / 100) * maxRating);
    }
    return 0;
  }

}
