package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Proposition;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.models.ReponseCandidatId;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReponseCandidatMapper {

    public static ReponseCandidatDto toDto(ReponseCandidat entity) {
        ReponseCandidatDto dto = new ReponseCandidatDto();
        dto.setIdCandidat(entity.getReponseCandidatId().getIdCandidat());
        dto.setIdProposition(entity.getReponseCandidatId().getIdProposition());
        return dto;
    }

    public static ReponseCandidat toEntity(ReponseCandidatDto reponseCandidatDto, PropositionRepository propositionRepository, CandidatRepository candidatRepository) {
        ReponseCandidatId reponseCandidatId = new ReponseCandidatId(reponseCandidatDto.getIdProposition(), reponseCandidatDto.getIdCandidat());
        Optional<Proposition> propositionOpt = propositionRepository.findById(reponseCandidatDto.getIdProposition());
        if(propositionOpt.isPresent()) {
            Proposition proposition = propositionOpt.get();

            Optional<Candidat> candidatOpt = candidatRepository.findById(reponseCandidatId.getIdCandidat());
            if(candidatOpt.isPresent()) {
                Candidat candidat = candidatOpt.get();

                return new ReponseCandidat(reponseCandidatId, proposition, candidat);
            }
        }
        return null;
    }
}
