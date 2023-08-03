package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import fr.mipih.rh.testcandidats.dtos.PersonneDTO;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.Privilege;
import fr.mipih.rh.testcandidats.models.Role;
import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
import fr.mipih.rh.testcandidats.repositories.RoleRepository;
import jakarta.transaction.Transactional;

@Service("userDetailsService")
@Transactional
public class AuthService implements UserDetailsService {
	
	@Autowired
	private PersonneRepository personneRepository;
	
	@Autowired
	private AdminRepository adminrepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
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

	private Collection<? extends GrantedAuthority> getAuthorities (List<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}
	
	private List<String> getPrivileges(Collection<Role> roles) {
		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
	
	
	private User construireUtilisateurPourAuthentification(Personne personne) {
		
		List<GrantedAuthority> autorities  = new ArrayList<>();
		
		autorities.add(new SimpleGrantedAuthority(personne.getRole().getName()));
		System.out.println(autorities);

		if("ROLE_ADMIN".equals(personne.getRole().getName())) {
			Admin admin = adminrepository.findByPersonne_Id(personne.getId());
			return new User(personne.getNom(), admin.getMotDePasse(), autorities );
		} else {
			autorities .add(new SimpleGrantedAuthority("ROLE_CANDIDAT"));
			return new User(personne.getNom(), "", autorities );
		}
	}

	public ConnexionStatus verifierMotDePasseAdmin(String nom, String motDePasse) {
		Personne personne = personneRepository.findByNom(nom);
		System.out.println(personne);
		if(personne == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		if ("ROLE_ADMIN".equals(personne.getRole().getName())) {
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
	
	public PersonneDTO personneInfo(String nom) {
		Personne personne = personneRepository.findByNom(nom);
		if(personne == null) {
			return null;
		}
		if("CANDIDAT".equals(personne.getRole().getName()) && personne.getCandidat().getTestId() == null) {
			return null;
		}
		PersonneDTO personneDTO = new PersonneDTO();
		personneDTO.setId(personne.getId());
		personneDTO.setNom(personne.getNom());
		personneDTO.setPrenom(personne.getPrenom());
		personneDTO.setRole(personne.getRole());
		if("CANDIDAT".equals(personne.getRole())) {
			personneDTO.setTest_id(personne.getCandidat().getTestId());
		}
		return personneDTO;
	}
	
	public ConnexionStatus verifierCandidat(String nom, String prenom) {
		Personne personne = personneRepository.findByNomAndPrenom(nom, prenom);
		if(personne == null || personne.getCandidat() == null) {
			return ConnexionStatus.UTILISATEUR_INCONNU;
		}
		System.out.println(personne.getRole());
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