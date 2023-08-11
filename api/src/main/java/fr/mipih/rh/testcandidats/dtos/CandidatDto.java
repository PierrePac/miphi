package fr.mipih.rh.testcandidats.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatDto {
	private Long id;
	private String nom;
	private String prenom;
	private String role;
	private String token;
	private String refreshToken;
	private Long testId; 
}
