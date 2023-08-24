package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReponseCandidatMapper {

    ReponseCandidatDto toDto(ReponseCandidat reponseCandidat);
    ReponseCandidat toEntity(ReponseCandidatDto reponseCandidatDto);
}
