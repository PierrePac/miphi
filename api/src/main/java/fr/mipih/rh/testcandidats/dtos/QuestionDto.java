package fr.mipih.rh.testcandidats.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {
	
	private Long id;
	private String question;
	private int point;
	private int temps;
	private String categorie;
	private String niveau;
	private String technologie;
	private ReponseDto[] reponses;
}
