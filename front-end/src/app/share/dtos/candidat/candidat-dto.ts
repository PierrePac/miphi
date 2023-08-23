export interface CandidatDto {
  id?: number;
  nom: string;
  prenom: string;
  role?: string;
  entretienId?: number;
  token?: string;
  refreshToken?: string;
}
