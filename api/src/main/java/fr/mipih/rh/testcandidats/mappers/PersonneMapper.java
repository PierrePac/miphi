package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.dtos.PersonneDto;
import fr.mipih.rh.testcandidats.models.Personne;

@Mapper(componentModel = "spring")
public interface PersonneMapper {

	PersonneDto toPersonneDto(Personne personne);
	
	Personne ajouterAdmin(NewAdminDto newAdminDto);
}
