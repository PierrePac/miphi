import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'consigneFormat'
})
export class ConsigneFormatPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) {}


  // Méthode pour splitter un string avec le marqueur ('--')
  // transform le string en une liste numéroté
  transform(value: string): SafeHtml {
    if (!value) {
      return '';
    }
    const consignes = value.split('--');
    let formattedConsignes = '';
    consignes.forEach((consigne, index) => {
      formattedConsignes += `<br><strong>${index + 1} -</strong> ${consigne.trim()}`;
    });
    return this.sanitizer.bypassSecurityTrustHtml(formattedConsignes);
  }

}
