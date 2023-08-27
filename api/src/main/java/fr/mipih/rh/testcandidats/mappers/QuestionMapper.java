package fr.mipih.rh.testcandidats.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.models.Proposition;
import fr.mipih.rh.testcandidats.models.enums.Categorie;
import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.models.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

	public static QuestionDto toDto(Question question) {
		if (question == null) {
			return null;
		}

		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(question.getId());
		questionDto.setQuestion(question.getQuestion());
		questionDto.setPoint(question.getPoint());
		questionDto.setTemps(question.getTemps());
		questionDto.setCategorie(question.getCategorie() != null ? question.getCategorie().name() : null);
		questionDto.setNiveau(question.getNiveau() != null ? question.getNiveau().name() : null);
		questionDto.setTechnologie(question.getTechnologie() != null ? question.getTechnologie().name() : null);

		List<PropositionDto> reponseDtos = new ArrayList<>();
		if (question.getPropositions() != null) {
			for (Proposition proposition : question.getPropositions()) {
				reponseDtos.add(PropositionMapper.toDto(proposition));
			}
		}
		questionDto.setReponses(reponseDtos.toArray(new PropositionDto[0]));

		return questionDto;
	}

	public static Question toEntity(QuestionDto questionDto) {
		if (questionDto == null) {
			return null;
		}

		Question question = new Question();
		question.setId(questionDto.getId());
		question.setQuestion(questionDto.getQuestion());
		question.setPoint(questionDto.getPoint());
		question.setTemps(questionDto.getTemps());
		question.setCategorie(questionDto.getCategorie() != null ? Categorie.valueOf(questionDto.getCategorie()) : null);
		question.setNiveau(questionDto.getNiveau() != null ? Niveau.valueOf(questionDto.getNiveau()) : null);
		question.setTechnologie(questionDto.getTechnologie() != null ? Technologie.valueOf(questionDto.getTechnologie()) : null);

		List<Proposition> propositions = new ArrayList<>();
		if (questionDto.getReponses() != null) {
			for (PropositionDto propositionDto : questionDto.getReponses()) {
				propositions.add(PropositionMapper.toEntity(propositionDto));
			}
		}
		question.setPropositions(new HashSet<>(propositions));

		return question;
	}

	public static List<QuestionDto> toDtoList(List<Question> questions) {
		List<QuestionDto> questionDtos = new ArrayList<>();
		for (Question question : questions) {
			questionDtos.add(toDto(question));
		}
		return questionDtos;
	}

	public static QuestionDto toGetAllDto(Question question) {
		if (question == null) {
			return null;
		}

		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(question.getId());
		questionDto.setQuestion(question.getQuestion());
		questionDto.setPoint(question.getPoint());
		questionDto.setTemps(question.getTemps());
		questionDto.setCategorie(question.getCategorie() != null ? question.getCategorie().name() : null);
		questionDto.setNiveau(question.getNiveau() != null ? question.getNiveau().name() : null);
		questionDto.setTechnologie(question.getTechnologie() != null ? question.getTechnologie().name() : null);

		return questionDto;
	}
}

