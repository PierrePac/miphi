export interface EntretienDto {
  id?: number;
  admin: {
    id: number | null;
  };
  date_end: string;
  date_start: string;
  qcm_id: {
      id: number;
  };
  sandbox_id: {
      id: number;
  };
}
