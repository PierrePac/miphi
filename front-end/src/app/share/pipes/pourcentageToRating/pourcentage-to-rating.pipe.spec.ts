import { PourcentageToRatingPipe } from './pourcentage-to-rating.pipe';

describe('PourcentageToRatingPipe', () => {
  it('create an instance', () => {
    const pipe = new PourcentageToRatingPipe();
    expect(pipe).toBeTruthy();
  });
});
