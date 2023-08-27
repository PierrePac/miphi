package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.*;
import fr.mipih.rh.testcandidats.models.*;
import fr.mipih.rh.testcandidats.repositories.*;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.CandidatMapper;
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
	private final QcmRepository qcmRepository;
	private final QuestionQcmRepository questionQcmRepository;
	private final QuestionRepository questionRepository;
	private final ReponseCandidatRepository reponseCandidatRepository;
	
	public CandidatDto loginCandidat(CredentialsCandidatDto credentialsDto) {
		Optional<Candidat> candidatOpt = candidatRepository.findByNom(credentialsDto.nom());
		if(candidatOpt.isPresent()){
			Candidat candidat = candidatOpt.get();
			if(candidat.getPrenom().equals(credentialsDto.prenom())) {
				if (candidat.getEntretien() != null) {
					return candidatMapper.toCandidatDto(candidat);
				}
				throw new AppException("Pas de test associé à ce candidat", HttpStatus.BAD_REQUEST);
			}
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
		Optional<Entretien> entretien = entretienRepository.findById(candidatDto.getEntretienId());
		if(entretien.isPresent())
			candidat.setEntretien(entretien.get());
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
		createReponseCandidat(newCandidatDto);
		return candidatMapper.toCandidatDto(candidat);
	}

	public List<CandidatDto> getAllCandidats() {
		return candidatRepository.findAll()
				.stream()
				.map(candidatMapper::toCandidatDto)
				.collect(Collectors.toList());
	}

	public void createReponseCandidat(NewCandidatDto newCandidatDto){
//		Optional<Entretien> entretienOpt = entretienRepository.findById(newCandidatDto.getEntretienId());
//		if(entretienOpt.isPresent()) {
//			Entretien entretien = entretienOpt.get();
//			Optional<Qcm> qcmOpt = qcmRepository.findById(entretien.getQcm().getId());
//			if(qcmOpt.isPresent()) {
//				Qcm qcm = qcmOpt.get();
//				Optional<List<QuestionQcm>> questionsQcmOpt = questionQcmRepository.findAllByQcmId(qcm.getId());
//				if(questionsQcmOpt.isPresent()){
//					List<QuestionQcm> questionQcmList = questionsQcmOpt.get();
//					Optional<Candidat> candidatOpt = candidatRepository.findByNomAndPrenom(newCandidatDto.getNom(), newCandidatDto.getPrenom());
//					if(candidatOpt.isPresent()){
//						Candidat candidat = candidatOpt.get();
//						for (QuestionQcm questionQcm: questionQcmList) {
//							Optional<Question> questionOpt = questionRepository.findById(questionQcm.getQuestion().getId());
//							if (questionOpt.isPresent()){
//								Question question = questionOpt.get();
//								ReponseCandidat reponseCandidat = new ReponseCandidat();
//								reponseCandidat.setCandidat(candidat);
//								reponseCandidatRepository.save(reponseCandidat);
//							}
//
//						}
//					}
//				}
//			}
//		}
	}
}
