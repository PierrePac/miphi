package fr.mipih.rh.testcandidats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.services.QcmService;

@RestController
@RequestMapping("/qcm")
public class QcmController {
	
	private final QcmRepository qcmRepository;
	private final QcmService qcmService;
	
	@Autowired
	public QcmController (QcmRepository qcmRepository, QcmService qcmService) {
		this.qcmService = qcmService;
		this.qcmRepository = qcmRepository;
	}
	
	@PostMapping("/add")
	public ResponseEntity<QcmDto> createQcm(@RequestBody QcmDto qcmDto) {
		QcmDto savedQcmDto = qcmService.saveQcm(qcmDto);
		return new ResponseEntity<>(savedQcmDto, HttpStatus.CREATED);
	}
}
