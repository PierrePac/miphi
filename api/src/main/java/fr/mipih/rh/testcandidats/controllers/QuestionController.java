package fr.mipih.rh.testcandidats.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.services.QuestionService;

@Controller
@RequestMapping("/api/questions")
public class QuestionController {

	private final QuestionService questionService;
	
	@Autowired
	public QuestionController(QuestionService questionService) {
		this.questionService = questionService;
	}

	@PostMapping
	public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
		return new ResponseEntity<>(questionService.saveQuestion(question), HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Question>> getAllQuestions() {
		return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id) {
		questionService.deleteQuestion(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
