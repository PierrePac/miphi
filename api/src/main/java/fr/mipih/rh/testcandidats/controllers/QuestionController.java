package fr.mipih.rh.testcandidats.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.mipih.rh.testcandidats.models.Proposition;
import lombok.RequiredArgsConstructor;
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
import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import fr.mipih.rh.testcandidats.services.QuestionService;
import fr.mipih.rh.testcandidats.services.PropositionService;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final PropositionRepository propositionRepository;
	private final PropositionService propositionService;

	@PostMapping("/add")
	public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
		QuestionDto savedQuestionDto = questionService.saveQuestion(questionDto);
		return new ResponseEntity<>(savedQuestionDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/modify/{id}")
	public ResponseEntity<QuestionDto> modifyQuestion(@RequestBody QuestionDto questionDto) {
		questionDto.setReponses(new PropositionDto[0]);
		QuestionDto savedQuestionDto = questionService.saveQuestion(questionDto);
		return new ResponseEntity<>(savedQuestionDto, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<QuestionDto>> getAllQuestions() {
		List<QuestionDto> questionDtos = questionService.getAllQuestions();

		List<PropositionDto> allReponses = propositionService.getAllReponses();
		Map<Long, List<PropositionDto>> questionIdToReponseDtosMap = allReponses.stream()
				.collect(Collectors.groupingBy(PropositionDto::getQuestion_id));

		for (QuestionDto questionDto : questionDtos) {
			List<PropositionDto> reponseDtosForQuestion = questionIdToReponseDtosMap.getOrDefault(questionDto.getId(), Collections.emptyList());
			questionDto.setReponses(reponseDtosForQuestion.toArray(new PropositionDto[0]));
		}
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}

	@GetMapping("/all-wr")
	public ResponseEntity<List<QuestionDto>> getAllQuestionsWithoutResponses() {
		List<QuestionDto> questionDtos = questionService.getAllQuestions();

		List<PropositionDto> allReponses = propositionService.getAllReponses();
		Map<Long, List<PropositionDto>> questionIdToReponseDtosMap = allReponses.stream()
				.collect(Collectors.groupingBy(PropositionDto::getQuestion_id));


		for (QuestionDto questionDto : questionDtos) {
			List<PropositionDto> reponseDtosForQuestion = questionIdToReponseDtosMap.getOrDefault(questionDto.getId(), Collections.emptyList());
			for(PropositionDto reponseDto: reponseDtosForQuestion) {
				reponseDto.setCorrect(false);
			}
			questionDto.setReponses(reponseDtosForQuestion.toArray(new PropositionDto[0]));
		}
		return new ResponseEntity<>(questionDtos, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id) {
		Question question = questionRepository.findById(id).orElse(null);
		if(question != null) {
			List<Proposition> propositions = propositionRepository.findAllByQuestionId(id);
			for(Proposition proposition: propositions) {
				propositionRepository.delete(proposition);
			}
			questionService.deleteQuestion(id);			
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
