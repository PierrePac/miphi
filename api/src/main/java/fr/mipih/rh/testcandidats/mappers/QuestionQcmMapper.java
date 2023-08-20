package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionQcmMapper {

    QuestionQcmDto toDto(QuestionQcm questionqcm);

    QuestionQcm toEntity(QuestionQcmDto questioqcmDto);
}
