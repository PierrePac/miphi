package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.models.Entretien;

@Mapper(componentModel = "spring")
public interface EntretienMapper {

    EntretienDto toEntretienDto(Entretien entretien);

    Entretien toEntretienEntity(EntretienDto entretienDto);

}
