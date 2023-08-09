package fr.mipih.rh.testcandidats.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.Reponse;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
	
	QuestionDto toDto(Question question);
	
	List<QuestionDto> toDtoList(List<Question> questions);

}
