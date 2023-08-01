package fr.mipih.rh.testcandidats.dtos;

import fr.mipih.rh.testcandidats.models.enums.Role;
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
	private Role role;
	private Long test_id;
	
}
