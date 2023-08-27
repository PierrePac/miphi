package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;

import fr.mipih.rh.testcandidats.models.Proposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.mappers.PropositionMapper;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;

@Service
public class PropositionService {

	private final PropositionRepository propositionRepository;

	@Autowired
	public PropositionService(PropositionRepository propositionRepository) {
		this.propositionRepository = propositionRepository;
	}

	public PropositionDto addReponse(PropositionDto propositionDto) {
		return PropositionMapper.toDto(propositionRepository.save(PropositionMapper.toEntity(propositionDto)));
	}

	public List<PropositionDto> getAllReponses() {
		List<Proposition> propositions = propositionRepository.findAll();
		List<PropositionDto> propositionDtos = new ArrayList<>();

		for (Proposition proposition : propositions) {
			propositionDtos.add(PropositionMapper.toGetAllDto(proposition));
		}

		return propositionDtos;
	}

	public void deleteReponse(Long id) {
		propositionRepository.deleteById(id);
	}

}
