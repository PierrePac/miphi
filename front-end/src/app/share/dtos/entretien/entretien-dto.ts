export interface EntretienDto {
  id?: number;
  admin: {
    id: number | null;
  };
  date_end: string;
  date_start: string;
  qcm: {
      id: number;
  };
  sandbox: {
      id: number;
  };
}
