package fr.mipih.rh.testcandidats.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionQcmDto {
	
	private Long id;
    private Long qcmId;
    private Long questionIds;
    private int ordre;
}