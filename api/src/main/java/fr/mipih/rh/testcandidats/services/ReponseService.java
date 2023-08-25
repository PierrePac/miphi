package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.mipih.rh.testcandidats.models.Reponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.mappers.ReponseMapper;
import fr.mipih.rh.testcandidats.repositories.ReponseRepository;

@Service
public class ReponseService {

	private final ReponseRepository reponseRepository;

	@Autowired
	public ReponseService(ReponseRepository reponseRepository) {
		this.reponseRepository = reponseRepository;
	}

	public ReponseDto addReponse(ReponseDto reponseDto) {
		return ReponseMapper.toDto(reponseRepository.save(ReponseMapper.toEntity(reponseDto)));
	}

	public List<ReponseDto> getAllReponses() {
		List<Reponse> reponses = reponseRepository.findAll();
		List<ReponseDto> reponseDtos = new ArrayList<>();

		for (Reponse reponse : reponses) {
			reponseDtos.add(ReponseMapper.toGetAllDto(reponse));
		}

		return reponseDtos;
	}

	public void deleteReponse(Long id) {
		reponseRepository.deleteById(id);
	}

}
