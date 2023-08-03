package fr.mipih.rh.testcandidats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.PersonneDTO;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.enums.ERole;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;

@Service
public class PersonneService {

	private final PersonneRepository personneRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public PersonneService(PersonneRepository personneRepository, PasswordEncoder passwordEncoder) {
		this.personneRepository = personneRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean enregistrerPersonne(PersonneDTO personneDTO) {
		Personne personne = convertirDTOenEntite(personneDTO);

		if (ERole.CANDIDAT.equals(personne.getRole())) {
			Candidat candidat = creerCandidat(personne);
			personne.setCandidat(candidat);
		} else if (ERole.ADMIN.equals(personne.getRole())) {
			Admin admin = creerAdmin(personne, personneDTO.getMotDePasse());
			personne.setAdmin(admin);
		}

		Personne personneEnregistree = personneRepository.save(personne);
		return personneEnregistree != null;
	}

	private Personne convertirDTOenEntite(PersonneDTO personneDTO) {
		Personne personne = new Personne();
		personne.setNom(personneDTO.getNom());
		personne.setPrenom(personneDTO.getPrenom());
		personne.setRole(personneDTO.getRole());

		return personne;
	}

	private Candidat creerCandidat(Personne personne) {
		Candidat candidat = new Candidat();
		candidat.setPersonne(personne);
		return candidat;
	}

	private Admin creerAdmin(Personne personne, String motDePasse) {
		Admin admin = new Admin();
		admin.setPersonne(personne);
		admin.setMotDePasse(passwordEncoder.encode(motDePasse));
		return admin;
	}
}