package fr.mipih.rh.testcandidats.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropositionDto {

	private Long id;
	private Long question_id;
	@JsonIgnore
	private QuestionDto question;
	private String reponse;
	private boolean correct;
}
