import { Niveau } from "../../enums/niveau.enum";
import { Technologie } from "../../enums/technologie.enum";

export interface OptionDto {
  technologie: Technologie;
  niveaux: Niveau[];
}
