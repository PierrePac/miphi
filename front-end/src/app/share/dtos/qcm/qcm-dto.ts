import { Question } from "../../models/question/question";

export interface QcmDto {
  id?: number,
  temps: number;
  point: number;
  nom: string;
  questions?: Question[];
}
