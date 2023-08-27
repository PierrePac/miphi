package fr.mipih.rh.testcandidats.controllers;

import java.util.Optional;

import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import fr.mipih.rh.testcandidats.services.PropositionService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/reponse")
@RequiredArgsConstructor
public class PropositionController {
	
	private final PropositionService propositionService;
	private final QuestionRepository questionRepository;
	private final QuestionMapper questionMapper;
	

	
	@PostMapping("/add")
	@Transactional
	public ResponseEntity<PropositionDto> addReponse(@RequestBody PropositionDto reponseDto) {
	    Optional<Question> optionalQuestion = questionRepository.findById(reponseDto.getQuestion_id());
	    
	    if (!optionalQuestion.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    reponseDto.setQuestion(questionMapper.toDto(optionalQuestion.get()));
	    PropositionDto savedReponseDto = propositionService.addReponse(reponseDto);
	    return new ResponseEntity<>(savedReponseDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/modify/{id}")
	public ResponseEntity<PropositionDto> modifyQuestion(@RequestBody PropositionDto reponseDto) {
	    Optional<Question> optionalQuestion = questionRepository.findById(reponseDto.getQuestion_id());
	    
	    if (!optionalQuestion.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    reponseDto.setQuestion(questionMapper.toDto(optionalQuestion.get()));
	    PropositionDto savedReponseDto = propositionService.addReponse(reponseDto);
	    return new ResponseEntity<>(savedReponseDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteReponse(@PathVariable("id") Long id) {
		propositionService.deleteReponse(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
