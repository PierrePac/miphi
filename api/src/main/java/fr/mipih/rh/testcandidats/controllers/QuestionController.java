package fr.mipih.rh.testcandidats.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.Reponse;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import fr.mipih.rh.testcandidats.repositories.ReponseRepository;
import fr.mipih.rh.testcandidats.services.QuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionMapper questionMapper;
	private final QuestionRepository questionRepository;
	private final ReponseRepository reponseRepository;
	
	@Autowired
	public QuestionController(	QuestionService questionService, 
								QuestionMapper questionMapper, 
								QuestionRepository questionRepository, 
								ReponseRepository reponseRepository) 
	{
		this.questionService = questionService;
		this.questionMapper = questionMapper;
		this.questionRepository = questionRepository;
		this.reponseRepository = reponseRepository;
		
	}

	@PostMapping("/add")
	public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
		Question question = questionMapper.toEntity(questionDto);
		Question savedQuestion = questionService.saveQuestion(question);
		QuestionDto savedQuestionDto = questionMapper.toDto(savedQuestion);
		return new ResponseEntity<>(savedQuestionDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/modify/{id}")
	public ResponseEntity<QuestionDto> modifyQuestion(@RequestBody QuestionDto questionDto) {
		Question question = questionMapper.toEntity(questionDto);
		Question savedQuestion = questionService.saveQuestion(question);
		QuestionDto savedQuestionDto = questionMapper.toDto(savedQuestion);
		return new ResponseEntity<>(savedQuestionDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<QuestionDto>> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		List<QuestionDto> questionDtos = questions.stream().map(questionMapper::toDto).collect(Collectors.toList());
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id) {
		Question question = questionRepository.findById(id).orElse(null);
		if(question != null) {
			List<Reponse> reponses = reponseRepository.findAllByQuestionId(id);
			for(Reponse reponse: reponses) {
				reponseRepository.delete(reponse);
			}
			questionService.deleteQuestion(id);			
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
