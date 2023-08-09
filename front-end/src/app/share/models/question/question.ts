import { Reponse } from "../reponse/reponse";

export class Question {
  id?: number;
  question!: string;
  point!: number;
  temps!: number;
  categorie!: string;
  niveau!: string;
  technologie!: string;
  reponses?: Reponse[] = []

constructor(data?: any) {
  if (data){
    this.id = data.id;
    this.question = data.question;
    this.point = data.point;
    this.temps = data.temps;
    this.categorie = data.categorie;
    this.niveau = data.niveau;
    this.technologie = data.technologie;
    if (data.reponses && Array.isArray(data.reponses)) {
       this.reponses = data.reponses.map((reponse: any) => new Reponse(reponse))
    }
  }
}
}
