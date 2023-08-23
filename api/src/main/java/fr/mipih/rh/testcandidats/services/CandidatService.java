package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.*;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.CandidatMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatService {

	private final CandidatMapper candidatMapper;
	private final CandidatRepository candidatRepository;
	private final EntretienRepository entretienRepository;
	
	public CandidatDto loginCandidat(CredentialsCandidatDto credentialsDto) {
		Candidat candidat = candidatRepository.findByNom(credentialsDto.nom())
				.orElseThrow(() -> new AppException("Candidat inconnu", HttpStatus.NOT_FOUND));
		if(candidat.getPrenom().equals(credentialsDto.prenom())) {
			if(candidat.getEntretien() != null) {
				return candidatMapper.toCandidatDto(candidat);
			}
			throw new AppException("Pas de test associé à ce candidat", HttpStatus.BAD_REQUEST);
		}
		throw new AppException("Candidat Inconnu", HttpStatus.BAD_REQUEST);
	}
	
	public CandidatDto findByNom(String nom) {
		Candidat candidat = candidatRepository.findByNom(nom)
				.orElseThrow(() -> new AppException("Candidat Inconnu", HttpStatus.NOT_FOUND));
		return candidatMapper.toCandidatDto(candidat);
	}
	
	public CandidatDto findByRefreshToken(String refreshToken) {
	    Candidat candidat = candidatRepository.findByRefreshToken(refreshToken)
	            .orElseThrow(() -> new AppException("Admin avec ce refreshToken inconnu", HttpStatus.NOT_FOUND));
	    return candidatMapper.toCandidatDto(candidat);
	}
	
	public CandidatDto save(CandidatDto candidatDto) {
        Candidat candidat = candidatMapper.toEntity(candidatDto);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toCandidatDto(candidat);
    }
	
	public void clearTokens(CandidatDto candidatDto) {
		candidatDto.setToken(null);
		candidatDto.setRefreshToken(null);
		save(candidatDto);
	}

	public CandidatDto ajoutCandidat(NewCandidatDto newCandidatDto) {
		Optional<Candidat> doublon = candidatRepository.findByNomAndPrenom(newCandidatDto.getNom(), newCandidatDto.getPrenom());
		if(doublon.isPresent()) {
			throw new AppException("Candidat déjà présent", HttpStatus.BAD_REQUEST);
		}
		Candidat candidat = new Candidat();
		candidat.setNom(newCandidatDto.getNom());
		candidat.setPrenom(newCandidatDto.getPrenom());

		Optional<Entretien> entretienOpt = entretienRepository.findById(newCandidatDto.getEntretienId());
		if(entretienOpt.isPresent()) {
			Entretien entretien = entretienOpt.get();
			candidat.setEntretien(entretien);
		} else {
			throw new AppException("Entretien non trouvé", HttpStatus.NOT_FOUND);
		}

		candidatRepository.save(candidat);
		return candidatMapper.toCandidatDto(candidat);
	}

	public List<CandidatDto> getAllCandidats() {
		return candidatRepository.findAll()
				.stream()
				.map(candidatMapper::toCandidatDto)
				.collect(Collectors.toList());
	}
}
