export interface ScoreDto {
  technologie: string;
  niveaux: {
    niveau: string;
    scoreTotal: number;
    scoreCandidat: number;
  }[];
}
