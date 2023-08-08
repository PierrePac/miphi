package fr.mipih.rh.testcandidats.dtos;

import fr.mipih.rh.testcandidats.models.Personne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto {

	private Long id;
	private Personne personne;
	private String motDePasse;
}
