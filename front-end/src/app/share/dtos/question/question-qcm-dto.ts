import { QuestionDto } from "./question-dto";

export interface QuestionQcmDto {
  id: number;
  ordre: number;
  question?: QuestionDto;
}
