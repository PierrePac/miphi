import { Niveau } from "../../enums/niveau.enum";
import { Technologie } from "../../enums/technologie.enum";

export interface SandboxDto {
  id?: number;
  src: string;
  consigne: string;
  nom: string;
  niveau: Niveau;
  technologie: Technologie;
}
