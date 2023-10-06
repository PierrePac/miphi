package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.models.Proposition;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.ReponseCandidatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	private final QuestionQcmRepository questionQcmRepository;
	private final PropositionRepository propositionRepository;
	private final ReponseCandidatRepository reponseCandidatRepository;

	public QuestionDto saveQuestion(QuestionDto questionDto) {
		return QuestionMapper.toDto(questionRepository.save(QuestionMapper.toEntity(questionDto)));
	}

	public List<QuestionDto> getAllQuestions() {
		List<Question> questions = questionRepository.findAll();
		List<QuestionDto> questionDtos = new ArrayList<>();

		for (Question question : questions) {
			questionDtos.add(QuestionMapper.toGetAllDto(question));
		}
		return questionDtos;
	}

	public void deleteQuestion(Long id) {
		Question question = questionRepository.findById(id).orElse(null);
		if (question != null) {
			List<Proposition> propositions = propositionRepository.findAllByQuestionId(id);
			for (Proposition proposition : propositions) {
				Optional<ReponseCandidat> reponseCandidatOptional = reponseCandidatRepository.findByReponseCandidatId_IdProposition(proposition.getId());
				if(reponseCandidatOptional.isPresent()){
					ReponseCandidat reponseCandidat = reponseCandidatOptional.get();
					reponseCandidatRepository.delete(reponseCandidat);
				}
				propositionRepository.delete(proposition);
			}
			List<QuestionQcm> questionQcmList = questionQcmRepository.findByQuestionQcmId_IdQuestion(id);
			for(QuestionQcm questionQcm: questionQcmList){
				questionQcmRepository.delete(questionQcm);
			}
			questionRepository.deleteById(id);
		}
	}

	public List<QuestionDto> getAllQuestionOfQcm(Long id) {
		List<QuestionQcm> questionQcmList = questionQcmRepository.findAllByQuestionQcmIdIdQcm(id);
		List<QuestionDto> questionDtoList = new ArrayList<>();

		for(QuestionQcm questionQcm: questionQcmList) {
			Optional<Question> questionOpt = questionRepository.findById(questionQcm.getQuestion().getId());
			if(questionOpt.isPresent()) {
				Question question = questionOpt.get();
				QuestionDto questionDto = QuestionMapper.toDto(question);
				questionDtoList.add(questionDto);
			}
		}
		return questionDtoList;
	}
}
