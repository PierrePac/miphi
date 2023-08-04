package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.Role;
import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService implements UserDetailsService {
	
	@Autowired
	private PersonneRepository personneRepository;
	
	@Autowired
	private AdminRepository adminrepository;
	
	@Autowired
	private CandidatRepository candidatRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String nom) throws UsernameNotFoundException {
		Personne personne = personneRepository.findByNom(nom);
		if (personne == null) {
			throw new UsernameNotFoundException("l'utilisateur n'a pas été trouvé avec le nom : : " + nom); 
		}
		return construireUtilisateurPourAuthentification(personne);
	}

	
	private User construireUtilisateurPourAuthentification(Personne personne) {
		
		List<GrantedAuthority> autorities  = new ArrayList<>();
		
		for(Role role : personne.getRoles()) {
			autorities.add(new SimpleGrantedAuthority(role.getName()));			
		}
		

		if(personne.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
			Admin admin = adminrepository.findByPersonne_Id(personne.getId());
			return new User(personne.getNom(), admin.getMotDePasse(), autorities );
		} else {
			autorities .add(new SimpleGrantedAuthority("ROLE_CANDIDAT"));
			return new User(personne.getNom(), "", autorities );
		}
	}

	public ConnexionStatus verifierMotDePasseAdmin(String nom, String motDePasse) {
		Personne personne = personneRepository.findByNom(nom);
		UserDetails userDetails = this.loadUserByUsername(nom);
		if(userDetails == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
			Admin admin = adminrepository.findByPersonne_Id(personne.getId());
			String motDePasseEnregistre = admin.getMotDePasse();
			if(!passwordEncoder.matches(motDePasse, motDePasseEnregistre)) {
				return ConnexionStatus.MOT_DE_PASSE_INCORRECT;
			} else {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return ConnexionStatus.ADMIN;
			}
		}
		return ConnexionStatus.UTILISATEUR_INCONNU;
	}
	
	public Admin adminInfo(String nom) {
		Personne personne = personneRepository.findByNom(nom);
		if(personne != null) {
			return adminrepository.findByPersonne(personne);
		}
		return null;
	}
	
	public Candidat candidatInfo(String nom) {
		Personne personne = personneRepository.findByNom(nom);
		if (personne != null) {
			return candidatRepository.findByPersonne(personne);
		}
		return null;
	}
	
	public ConnexionStatus verifierCandidat(String nom, String prenom) {
		UserDetails userDetails = this.loadUserByUsername(nom);
		if(userDetails == null || userDetails.getAuthorities().stream().noneMatch(role -> role.getAuthority().contains("ROLE_CANDIDAT"))) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return ConnexionStatus.CANDIDAT;
	}
	
	public String checkRoles( ) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    StringBuilder roles = new StringBuilder();
	    for (GrantedAuthority authority : auth.getAuthorities()) {
	        roles.append("Rôle : ").append(authority.getAuthority()).append("\n");
	    }
	    return roles.toString();
	}

}