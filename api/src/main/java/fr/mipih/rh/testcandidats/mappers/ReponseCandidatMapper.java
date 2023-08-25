package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReponseCandidatMapper {

    private final CandidatMapper candidatMapper;
    private final QuestionMapper questionMapper;

    public ReponseCandidatDto toDto(ReponseCandidat reponseCandidat) {
        if (reponseCandidat == null) {
            return null;
        }

        ReponseCandidatDto reponseCandidatDto = new ReponseCandidatDto();
        reponseCandidatDto.setId(reponseCandidat.getId());
        reponseCandidatDto.setCandidat(candidatMapper.toCandidatDto(reponseCandidat.getCandidat()));
        reponseCandidatDto.setQuestion(questionMapper.toDto(reponseCandidat.getQuestion()));
        // Conversion des autres champs, si nécessaire

        return reponseCandidatDto;
    }

    public ReponseCandidat toEntity(ReponseCandidatDto reponseCandidatDto) {
        if (reponseCandidatDto == null) {
            return null;
        }

        ReponseCandidat reponseCandidat = new ReponseCandidat();
        reponseCandidat.setId(reponseCandidatDto.getId());
        reponseCandidat.setCandidat(candidatMapper.toEntity(reponseCandidatDto.getCandidat()));
        reponseCandidat.setQuestion(questionMapper.toEntity(reponseCandidatDto.getQuestion()));
        // Conversion des autres champs, si nécessaire

        return reponseCandidat;
    }

}
