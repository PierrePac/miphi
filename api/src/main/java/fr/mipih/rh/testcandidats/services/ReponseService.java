package fr.mipih.rh.testcandidats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.models.Reponse;
import fr.mipih.rh.testcandidats.repositories.ReponseRepository;

@Service
public class ReponseService {
	
private final ReponseRepository reponseRepository;
	
	@Autowired
	public ReponseService(ReponseRepository reponseRepository) {
		this.reponseRepository = reponseRepository;
	}
	
	public Reponse addReponse(Reponse reponse) {
		return reponseRepository.save(reponse);
	}
	

	public void deleteReponse(Long id) {
		reponseRepository.deleteById(id);
	}

}
