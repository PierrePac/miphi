export class Question {
  id?: number;
  question!: string;
  points!: number;
  temps!: number;
  categorie!: string;
  niveau!: string;
  technologie!: string;

constructor(data?: any) {
  if (data){
    this.id = data.id;
    this.question = data.question;
    this.points = data.points;
    this.temps = data.temps;
    this.categorie = data.categorie;
    this.niveau = data.niveau;
    this.technologie = data.technologie;
  }
}
}
