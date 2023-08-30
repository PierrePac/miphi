export interface CorrectAnswerDto {
  question_id: number;
  correctAnswer: number[];
  incorrectAnswer: number[];
  technologie: string;
  point: number;
}
