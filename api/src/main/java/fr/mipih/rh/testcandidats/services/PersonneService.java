package fr.mipih.rh.testcandidats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.PersonneDTO;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.models.enums.Role;
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
	
	public ConnexionStatus verifierMotDePasse(String nom, String motDePasse) {
		String motDePasseEnregistre = personneRepository.findMotDePasseByNom(nom);
		if(motDePasseEnregistre == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		} else if (!passwordEncoder.matches(motDePasse, motDePasseEnregistre)) {
			return ConnexionStatus.MOT_DE_PASSE_INCORRECT;
		} else {
			Personne personne = personneRepository.findByNom(nom);
			if (personne != null) {
				if(personne.getAdmin() != null) {
					return ConnexionStatus.ADMIN;
				} else if (personne.getCandidat() != null) {
					return ConnexionStatus.CANDIDAT;
				} else {
					return ConnexionStatus.UTILISATEUR_INCONNU;
				}	
			}
			
		}
		return ConnexionStatus.UTILISATEUR_INCONNU;
	}

	public boolean enregistrerPersonne(PersonneDTO personneDTO) {
		Personne personne = convertirDTOenEntite(personneDTO);
		
		if (Role.CANDIDAT.equals(personne.getRole())) {
			Candidat candidat = creerCandidat(personne);
			personne.setCandidat(candidat);
		} else if (Role.ADMIN.equals(personne.getRole())) {
			Admin admin = creerAdmin(personne);
			personne.setAdmin(admin);
		}
		
		Personne personneEnregistree = personneRepository.save(personne);
		return personneEnregistree != null;
	}
	
	private Personne convertirDTOenEntite(PersonneDTO personneDTO) {
		Personne personne = new Personne();
		personne.setNom(personneDTO.getNom());
		personne.setPrenom(personneDTO.getPrenom());
		personne.setMotDePasse(passwordEncoder.encode(personneDTO.getMotDePasse()));
		personne.setRole(personneDTO.getRole());
		
		return personne;	
	}
	
	private Candidat creerCandidat(Personne personne) {
		Candidat candidat = new Candidat();
		candidat.setPersonne(personne);
		return candidat;
	}
	
	private Admin creerAdmin(Personne personne) {
		Admin admin = new Admin();
		admin.setPersonne(personne);
		return admin;
	}
}
