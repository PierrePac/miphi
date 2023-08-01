package fr.mipih.rh.testcandidats.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
@Service
public class SecurityUserDetailService implements UserDetailsService {
	
	@Autowired
	private PersonneRepository personneRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Personne personne = personneRepository.findByNom(username);
		
		if (personne == null) {
			throw new UsernameNotFoundException("L'utilisateur n'a pas été touvé");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		String password = "";
		if(personne.getAdmin() != null) {
			password = personne.getAdmin().getMotDePasse();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (personne.getCandidat() != null) {
			authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDAT"));
		}
		
		return new User(personne.getNom(), password, Collections.emptyList());
	}

}
