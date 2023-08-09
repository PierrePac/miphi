package fr.mipih.rh.testcandidats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.mappers.ReponseMapper;
import fr.mipih.rh.testcandidats.models.Reponse;
import fr.mipih.rh.testcandidats.services.ReponseService;

@Controller
@RequestMapping("/api")
public class ReponseController {
	
	private ReponseService reponseService;
	private final ReponseMapper reponseMapper;
	
	@Autowired
	public ReponseController(ReponseService reponseService, ReponseMapper reponseMapper) {
		this.reponseService = reponseService;
		this.reponseMapper = reponseMapper;
	}
	
	@PostMapping("/add-reponse")
	public ResponseEntity<ReponseDto> addReponse(@RequestBody ReponseDto reponseDto) {
	    Reponse reponse = reponseMapper.toEntity(reponseDto);
	    Reponse savedReponse = reponseService.addReponse(reponse);
	    ReponseDto savedReponseDto = reponseMapper.toDto(savedReponse);
	    return new ResponseEntity<>(savedReponseDto, HttpStatus.CREATED);
	}

	
	@DeleteMapping("/delete-reponse/{id}")
	public ResponseEntity<?> deleteReponse(@PathVariable("id") Long id) {
		reponseService.deleteReponse(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
