package fr.mipih.rh.testcandidats.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.models.Reponse;

@Mapper(componentModel = "spring")
public interface ReponseMapper {

	@Mapping(source = "reponse.question.id", target = "question_id")
    ReponseDto toDto(Reponse reponse);
    
    Reponse toEntity(ReponseDto reponseDto);
    
    List<ReponseDto> toDtoList(List<Reponse> reponses);
    
    @Mapping(source = "dto", target = "entity")
    List<Reponse> toEntityList(List<ReponseDto> dtos);
	
}
