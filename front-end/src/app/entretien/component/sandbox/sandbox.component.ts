import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { SandboxService } from 'src/app/core/services/sandbox/sandbox.service';
import { ConsignesDto } from 'src/app/share/dtos/consignes/consigne-dto';

@Component({
  selector: 'app-sandbox',
  templateUrl: './sandbox.component.html',
  styleUrls: ['./sandbox.component.scss']
})
export class SandboxComponent implements OnInit{
  url!: SafeResourceUrl;
  idEntretien!: number;
  consignes: ConsignesDto = { consignes: [] };
  currentConsigneIndex: number = 0;


  constructor(private sanitizer: DomSanitizer,
              private sandboxService: SandboxService,
              private router: Router) {
    // Initialisation de l'url sécurisée pour l'iframe
    this.url = this.sanitizer.bypassSecurityTrustResourceUrl('http://p2lxtdkear01.mipih.net:8443/?folder=/config/workspace')
  }
  ngOnInit(): void {
    // Récupération de l'objet 'personne' de la session et de l'id de l'entretien
    const personneJSON = sessionStorage.getItem('personne');
    const personne = personneJSON ? JSON.parse(personneJSON) : null;
    this.idEntretien = personne.entretienId;
    // récupération des consignes de l'entretien
    this.getConsigne(this.idEntretien);
  }

  // récupération des consignes de l'entretien
  getConsigne(idEntretien: number) {
    this.sandboxService.getConsigne(idEntretien).subscribe(resp => {
      let reponse: string = resp.consignes;
      this.consignes.consignes = reponse.split('--');
    })
  }

  // affichage des consignes suivantes
  nextConsigne() {
    if (this.currentConsigneIndex < this.consignes.consignes.length - 1) {
      this.currentConsigneIndex++;
    } else {
      this.router.navigate(['/candidat'], { queryParams: { section: 'finish' } });
    }
  }
}
