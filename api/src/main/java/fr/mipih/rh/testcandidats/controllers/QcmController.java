package fr.mipih.rh.testcandidats.controllers;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.AddQuestionToQcmDto;
import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.services.QcmService;

@RestController
@RequestMapping("/qcm")
@RequiredArgsConstructor
public class QcmController {

	private final QcmService qcmService;
	
	@PostMapping("/add")
	public ResponseEntity<QcmDto> createQcm(@RequestBody QcmDto qcmDto) {
		QcmDto savedQcmDto = qcmService.saveQcm(qcmDto);
		return new ResponseEntity<>(savedQcmDto, HttpStatus.CREATED);
	}

	@PostMapping("/add-question")
	public ResponseEntity<Void> addQuestionToQcm(@RequestBody AddQuestionToQcmDto request) {
		Long qcmId = request.getQcmId();
		int ordre = 0;
		for(Long questionId: request.getQuestionIds()) {
			qcmService.addQuestionToQcm(qcmId, questionId, ordre);
			ordre ++;
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<QcmDto>> getAllQcm() {
		List<QcmDto> qcms = qcmService.getAllQcms();
		return new ResponseEntity<>(qcms, HttpStatus.OK);
	}
	
	@PostMapping("/update-order")
	public ResponseEntity<List<QuestionQcmDto>> updateOrder(@RequestBody List<QuestionQcmDto> questionQcmDtos) {
		List<QuestionQcmDto> updatedQuestions  = new ArrayList<>();
		for(QuestionQcmDto questionQcmDto: questionQcmDtos) {
			QuestionQcmDto updatedQuestion = qcmService.updateOrder(questionQcmDto);
			updatedQuestions.add(updatedQuestion);
		}
		return new ResponseEntity<>(updatedQuestions, HttpStatus.OK);
	}
}
