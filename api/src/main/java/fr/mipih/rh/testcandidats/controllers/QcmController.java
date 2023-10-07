package fr.mipih.rh.testcandidats.controllers;

import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.mipih.rh.testcandidats.dtos.AddQuestionToQcmDto;
import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.services.QcmService;

@RestController
@RequestMapping("/qcm")
@RequiredArgsConstructor
public class QcmController {

	private final QcmService qcmService;
	private final EntretienRepository entretienRepository;
	
	@PostMapping("/add")
	public ResponseEntity<QcmDto> createQcm(@RequestBody QcmDto qcmDto) {
		QcmDto savedQcmDto = qcmService.saveQcm(qcmDto);
		return new ResponseEntity<>(savedQcmDto, HttpStatus.CREATED);
	}

	@PostMapping("/add-question")
	public ResponseEntity<Void> addQuestionToQcm(@RequestBody AddQuestionToQcmDto request) {
		Long qcmId = request.getQcmId();
		Long ordre = 0L;
		for(Long questionId: request.getQuestionIds()) {
			qcmService.addQuestionToQcm(qcmId, questionId, ordre);
			ordre ++;
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<QcmDto>> getAllQcm() {
		List<QcmDto> qcms = qcmService.getAllQcm();
		return new ResponseEntity<>(qcms, HttpStatus.OK);
	}

	@GetMapping("/get-by-entretien/{id}")
	public ResponseEntity<QcmDto> getQcmByEntretienId(@PathVariable Long id) {
		Optional<Entretien> entretienOpt = entretienRepository.findById(id);
		if(entretienOpt.isPresent()){
			Entretien entretien = entretienOpt.get();
			QcmDto qcmDto = qcmService.getQcm(entretien.getQcm().getId());
			return new ResponseEntity<>(qcmDto, HttpStatus.OK);
		}
		return null;
	}
	



}
