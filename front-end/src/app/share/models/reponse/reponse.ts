export class Reponse {
  id?: number;
  questionId!: number;
  reponse!: string;
  correct!: boolean;

  constructor(data?: any) {
    if(data){
      this.id = data.id
      this.questionId = data.question_id;
      this.reponse = data.response;
      this.correct = data.correct
    }
  }
}
