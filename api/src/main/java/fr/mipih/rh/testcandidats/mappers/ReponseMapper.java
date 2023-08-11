package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.models.Reponse;

@Mapper(componentModel = "spring")
public interface ReponseMapper {

    ReponseDto toDto(Reponse reponse);
    
    Reponse toEntity(ReponseDto reponseDto);
	
}
