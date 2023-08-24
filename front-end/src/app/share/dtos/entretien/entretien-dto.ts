export interface EntretienDto {
  id?: number;
  admin: {
    id: number | null;
  };
  date_end: string;
  date_start: string;
  qcm: {
      questions: any;
      id: number;
      point: number;
      temps: number;
  };
  sandbox: {
      id: number;
  };
}
