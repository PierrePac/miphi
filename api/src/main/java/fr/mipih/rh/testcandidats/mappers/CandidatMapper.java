package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.models.Entretien;
import jakarta.persistence.DiscriminatorValue;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewCandidatDto;
import fr.mipih.rh.testcandidats.models.Candidat;
import org.springframework.stereotype.Component;

@Component
public class CandidatMapper {

	public CandidatDto toCandidatDto(Candidat candidat) {
		if (candidat == null) {
			return null;
		}

		CandidatDto dto = new CandidatDto();
		dto.setId(candidat.getId());
		dto.setNom(candidat.getNom());
		dto.setPrenom(candidat.getPrenom());
		dto.setRole(candidat.getClass().getAnnotation(DiscriminatorValue.class).value());
		dto.setToken(candidat.getToken());
		dto.setRefreshToken(candidat.getRefreshToken());

		Entretien entretien = candidat.getEntretien();
		if (entretien != null) {
			dto.setEntretienId(entretien.getId());
		}

		return dto;
	}

	public Candidat toEntity(CandidatDto candidatDto) {
		if (candidatDto == null) {
			return null;
		}

		Candidat entity = new Candidat();
		entity.setId(candidatDto.getId());
		entity.setNom(candidatDto.getNom());
		entity.setPrenom(candidatDto.getPrenom());
		entity.setToken(candidatDto.getToken());
		entity.setRefreshToken(candidatDto.getRefreshToken());

		return entity;
	}

	public Candidat ajouterCandidat(NewCandidatDto newCandidatDto) {
		if (newCandidatDto == null) {
			return null;
		}

		Candidat entity = new Candidat();
		entity.setNom(newCandidatDto.getNom());
		entity.setPrenom(newCandidatDto.getPrenom());

		return entity;
	}

}
