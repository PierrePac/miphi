package fr.mipih.rh.testcandidats.dtos;

import java.util.Collection;

import fr.mipih.rh.testcandidats.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonneDTO {
	
	private Long id;
	private String nom;
	private String prenom;
	private String motDePasse;
	private Collection<Role> roles;
	private Long test_id;
	
}
