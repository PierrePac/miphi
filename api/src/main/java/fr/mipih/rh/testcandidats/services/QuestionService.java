package fr.mipih.rh.testcandidats.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;

@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	private final QuestionMapper questionMapper;
	
	@Autowired
	public QuestionService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
		this.questionRepository = questionRepository;
		this.questionMapper = questionMapper;
	}

	public QuestionDto saveQuestion(QuestionDto questionDto) {
		return QuestionMapper.toDto(questionRepository.save(QuestionMapper.toEntity(questionDto)));
	}

	public List<QuestionDto> getAllQuestions() {
		return QuestionMapper.toDtoList(questionRepository.findAll());
	}

	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);
	}
}
