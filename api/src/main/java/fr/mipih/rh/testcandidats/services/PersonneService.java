package fr.mipih.rh.testcandidats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.PersonneDTO;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.Role;
import fr.mipih.rh.testcandidats.models.enums.ERole;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
import fr.mipih.rh.testcandidats.repositories.RoleRepository;

@Service
public class PersonneService {

	private final PersonneRepository personneRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	@Autowired
	public PersonneService(PersonneRepository personneRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.personneRepository = personneRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public boolean enregistrerPersonne(PersonneDTO personneDTO) {
		Personne personne = convertirDTOenEntite(personneDTO);

		if (personneDTO.getRoles().contains(ERole.ROLE_CANDIDAT)) {
			Candidat candidat = creerCandidat(personne);
			personne.setCandidat(candidat);
		} else if (personneDTO.getRoles().contains(ERole.ROLE_ADMIN)) {
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
		for (Role role : personneDTO.getRoles() ) {
			Role r = roleRepository.findByName(role.getName());
			if(r==null) {
				r = new Role();
				r.setName(role.getName());
				roleRepository.save(r);
			}
			personne.getRoles().add(r);
			r.getPersonnes().add(personne);
		}
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