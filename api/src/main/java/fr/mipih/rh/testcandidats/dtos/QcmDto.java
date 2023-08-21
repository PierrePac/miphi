package fr.mipih.rh.testcandidats.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QcmDto {
	private Long id;
	private String nom;
	private int point;
	private int temps;
	private List<QuestionQcmDto> questions;

}
