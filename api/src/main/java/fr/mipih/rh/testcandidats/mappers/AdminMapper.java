package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.models.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

	AdminDto toAdminDto(Admin admin);
	
	@Mapping(target = "motDePasse", ignore = true)
	Admin ajouterAdmin(NewAdminDto newAdminDto);
}
