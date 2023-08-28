package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.List;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
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
	private final QuestionQcmMapper questionQcmMapper;

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

	public List<QuestionQcmDto> getQuestionQcm(Long qcmId) {
		List<QuestionQcm> questionQcmList = questionQcmRepository.findAllByQuestionQcmIdIdQcm(qcmId);
		List<QuestionQcmDto> questionQcmListDto = new ArrayList<>();
		for (QuestionQcm questionQcm : questionQcmList) {
			QuestionQcmDto dto = questionQcmMapper.toDto(questionQcm);
			questionQcmListDto.add(dto);
		}
		return questionQcmListDto;
	}

	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);
	}
}
