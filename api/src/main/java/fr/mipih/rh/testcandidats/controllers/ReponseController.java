package fr.mipih.rh.testcandidats.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.mappers.ReponseMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.Reponse;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import fr.mipih.rh.testcandidats.services.ReponseService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/reponse")
public class ReponseController {
	
	private ReponseService reponseService;
	private final ReponseMapper reponseMapper;
	private final QuestionRepository questionRepository;
	
	@Autowired
	public ReponseController(ReponseService reponseService, ReponseMapper reponseMapper, QuestionRepository questionRepository) {
		this.reponseService = reponseService;
		this.reponseMapper = reponseMapper;
		this.questionRepository = questionRepository;
	}
	
	@PostMapping("/add")
	@Transactional
	public ResponseEntity<ReponseDto> addReponse(@RequestBody ReponseDto reponseDto) {
	    Reponse reponse = reponseMapper.toEntity(reponseDto);

	    Optional<Question> optionalQuestion = questionRepository.findById(reponseDto.getQuestion_id());
	    if (!optionalQuestion.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    reponse.setQuestion(optionalQuestion.get());
	    Reponse savedReponse = reponseService.addReponse(reponse);
	    ReponseDto savedReponseDto = reponseMapper.toDto(savedReponse);
	    return new ResponseEntity<>(savedReponseDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/modify/{id}")
	public ResponseEntity<ReponseDto> modifyQuestion(@RequestBody ReponseDto reponseDto) {
		Reponse reponse = reponseMapper.toEntity(reponseDto);

	    Optional<Question> optionalQuestion = questionRepository.findById(reponseDto.getQuestion_id());
	    if (!optionalQuestion.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    reponse.setQuestion(optionalQuestion.get());
	    Reponse savedReponse = reponseService.addReponse(reponse);
	    ReponseDto savedReponseDto = reponseMapper.toDto(savedReponse);
	    return new ResponseEntity<>(savedReponseDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteReponse(@PathVariable("id") Long id) {
		reponseService.deleteReponse(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
