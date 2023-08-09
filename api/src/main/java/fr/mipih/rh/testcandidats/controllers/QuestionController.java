package fr.mipih.rh.testcandidats.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.services.QuestionService;

@RestController
@RequestMapping("/api")
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionMapper questionMapper;
	
	@Autowired
	public QuestionController(QuestionService questionService, QuestionMapper questionMapper) {
		this.questionService = questionService;
		this.questionMapper = questionMapper;
		
	}

	@PostMapping("/add-question")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
		return new ResponseEntity<>(questionService.saveQuestion(question), HttpStatus.CREATED);
	}
	
	@GetMapping("/all-questions")
	public ResponseEntity<List<QuestionDto>> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		List<QuestionDto> questionDtos = questions.stream().map(questionMapper::toDto).collect(Collectors.toList());
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete-question/{id}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id) {
		questionService.deleteQuestion(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
