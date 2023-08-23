export interface FullEntretienDto {
    id?: number;
    admin: {
      id: number | null;
      nom?: string;
      prenom?: string;
      refreshToken?: string;
      role?: string | null;
      token?: string | null;
    };
    date_end?: string;
    date_start?: string;
    qcm: {
      id: number;
      nom?: string;
      point?: number;
      temps?: number;
      questions?: Array<{
        id?: number;
        ordre?: number;
        qcmId?: number | null;
        questionIds?: number[] | null;
      }>;
    };
    sandbox?: {
      categorie?: string | null;
      id?: number;
      niveau?: string;
      src?: string;
      technologie?: string;
    };
}
