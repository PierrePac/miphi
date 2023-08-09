import { ReponseDto } from "../reponse/reponse-dto";

export interface QuestionDto {
  id?: number;
  temps: number;
  point: number;
  question: string;
  categorie: string;
  technologie: string;
  niveau: string;
  reponses?: ReponseDto[];
}
