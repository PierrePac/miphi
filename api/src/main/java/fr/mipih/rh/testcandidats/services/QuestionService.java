package fr.mipih.rh.testcandidats.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;

@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	@Autowired
	public QuestionService(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public Question saveQuestion(Question question) {
		return questionRepository.save(question);
	}
	
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}

	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);
	}
}
