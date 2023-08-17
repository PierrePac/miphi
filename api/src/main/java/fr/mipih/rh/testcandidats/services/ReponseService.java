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
	private final ReponseMapper reponseMapper;
	
	@Autowired
	public ReponseService(ReponseRepository reponseRepository, ReponseMapper reponseMapper) {
		this.reponseRepository = reponseRepository;
		this.reponseMapper = reponseMapper;
	}
	
	public ReponseDto addReponse(ReponseDto reponseDto) {
		return reponseMapper.toDto(reponseRepository.save(reponseMapper.toEntity(reponseDto)));
	}
	
	public List<ReponseDto> getAllReponses() {
		return reponseRepository.findAll()
				.stream()
				.map(reponseMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public void deleteReponse(Long id) {
		reponseRepository.deleteById(id);
	}

}
