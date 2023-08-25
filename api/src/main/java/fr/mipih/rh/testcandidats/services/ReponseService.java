package fr.mipih.rh.testcandidats.services;

import java.util.List;
import java.util.stream.Collectors;

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
		return ReponseMapper.toDtoList(reponseRepository.findAll());
	}

	public void deleteReponse(Long id) {
		reponseRepository.deleteById(id);
	}

}
