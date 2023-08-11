package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewCandidatDto;
import fr.mipih.rh.testcandidats.models.Candidat;

@Mapper(componentModel = "spring")
public interface CandidatMapper {
	
	CandidatDto toCandidatDto(Candidat candidat);
	
	@Mapping(target = "entretienId", ignore = true)
	Candidat ajouterCandidat(NewCandidatDto newCandidatDto);
	
	Candidat toEntity(CandidatDto adminDto);

}
