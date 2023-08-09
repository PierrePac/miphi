package fr.mipih.rh.testcandidats.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.CandidatMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidatService {

	private final CandidatMapper candidatMapper;
	private final CandidatRepository candidatRepository;
	
	public CandidatDto loginCandidat(CredentialsCandidatDto credentialsDto) {
		Candidat candidat = candidatRepository.findByNom(credentialsDto.nom())
				.orElseThrow(() -> new AppException("Candidat inconnu", HttpStatus.NOT_FOUND));
		if(candidat.getPrenom().equals(credentialsDto.prenom())) {
			if(candidat.getEntretienId() != null) {
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
}