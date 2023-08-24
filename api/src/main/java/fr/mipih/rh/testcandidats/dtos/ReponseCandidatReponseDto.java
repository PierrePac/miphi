package fr.mipih.rh.testcandidats.dtos;

import fr.mipih.rh.testcandidats.models.Reponse;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReponseCandidatReponseDto {
    private ReponseCandidat reponseCandidat;
    private Reponse reponse;
}
