import { QuestionDto } from "../question/question-dto";

export interface FilteredQuestionsData {
  questions: QuestionDto[];
  totalTime: number;
  totalPoints: number;
}
