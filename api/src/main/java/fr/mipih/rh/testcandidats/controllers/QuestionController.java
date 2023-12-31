package fr.mipih.rh.testcandidats.controllers;

import java.util.*;
import java.util.stream.Collectors;
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
import fr.mipih.rh.testcandidats.services.QuestionService;
import fr.mipih.rh.testcandidats.services.PropositionService;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
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
			List<PropositionDto> reponseDtosForQuestion =
					questionIdToReponseDtosMap.getOrDefault(questionDto.getId(), Collections.emptyList());
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
		questionService.deleteQuestion(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/get-by-qcm/{id}")
	public ResponseEntity<List<QuestionDto>> getQuestionByQcmId(@PathVariable("id") Long id) {
		List<QuestionDto> questionDtoList = questionService.getAllQuestionOfQcm(id);
		return new ResponseEntity<>(questionDtoList, HttpStatus.OK);
	}


}
