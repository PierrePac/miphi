package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;

@Service
public class AuthService implements UserDetailsService {

	private final PersonneRepository personneRepository;
	private final AdminRepository adminrepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthService(PersonneRepository personneRepository, AdminRepository adminrepository, PasswordEncoder passwordEncoder) {
		this.personneRepository = personneRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminrepository = adminrepository;
	}

	@Override
	public UserDetails loadUserByUsername(String nom) throws UsernameNotFoundException {
		Personne personne = personneRepository.findByNom(nom);
		if (personne == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom : nom");
		}
		return construireUtilisateurPourAuthentification(personne);
	}

	private User construireUtilisateurPourAuthentification(Personne personne) {
		Admin admin = null;
		if("ADMIN".equals(personne.getRole().toString())) {
			admin = adminrepository.findByPersonne_Id(personne.getId());
		}
		List<GrantedAuthority> autorities = new ArrayList<>();
		autorities.add(new SimpleGrantedAuthority(personne.getRole().toString()));
		if ("ADMIN".equals(personne.getRole().toString())) {
			return new User(personne.getNom(), admin.getMotDePasse(), autorities);
		} else {
			return new User(personne.getNom(), personne.getPrenom(), autorities);
		}
		
	}

	public ConnexionStatus verifierMotDePasseAdmin(String nom, String motDePasse) {
		Personne personne = personneRepository.findByNom(nom);
		if(personne == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		if ("ADMIN".equals(personne.getRole().toString())) {
			Admin admin = adminrepository.findByPersonne_Id(personne.getId());
			String motDePasseEnregistre = admin.getMotDePasse();
			if(!passwordEncoder.matches(motDePasse, motDePasseEnregistre)) {
				return ConnexionStatus.MOT_DE_PASSE_INCORRECT;
			} else {
				return ConnexionStatus.ADMIN;
			}
		}
		return ConnexionStatus.UTILISATEUR_INCONNU;
		
	}
	
	public ConnexionStatus verifierCandidat(String nom, String prenom) {
		Personne personne = personneRepository.findByNomAndPrenom(nom, prenom);
		if(personne == null || personne.getCandidat() == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		return ConnexionStatus.CANDIDAT;
		
	}
}