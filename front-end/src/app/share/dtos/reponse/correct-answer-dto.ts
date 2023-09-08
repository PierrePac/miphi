export interface CorrectAnswerDto {
  question_id: number;
  correctAnswer: number[];
  incorrectAnswer: number[];
  niveau: string;
  technologie: string;
  point: number;
}
