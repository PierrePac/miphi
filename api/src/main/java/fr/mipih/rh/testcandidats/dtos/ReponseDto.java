package fr.mipih.rh.testcandidats.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.mipih.rh.testcandidats.models.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReponseDto {

	private Long id;
	private Long question_id;
	@JsonIgnore
	private Question question;
	private String reponse;
	private boolean correct;
}
