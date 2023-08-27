package fr.mipih.rh.testcandidats.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

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
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int point;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int temps;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String categorie;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String niveau;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String technologie;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private PropositionDto[] reponses;
}
