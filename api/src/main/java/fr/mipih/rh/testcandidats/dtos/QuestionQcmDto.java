package fr.mipih.rh.testcandidats.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionQcmDto {
	
	private Long idQcm;
    private Long idQuestion;
    private Long ordre;
}
