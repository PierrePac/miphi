package fr.mipih.rh.testcandidats.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import fr.mipih.rh.testcandidats.services.QcmService;
import fr.mipih.rh.testcandidats.services.QuestionQcmService;
import fr.mipih.rh.testcandidats.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question-qcm")
@RequiredArgsConstructor
public class QuestionQcmController {

    private final QuestionService questionService;
    private final QuestionQcmService questionQcmService;
    private final EntretienRepository entretienRepository;

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionQcmDto>> getQcmQuestionOfQcm(@PathVariable("id") Long id) {
        List<QuestionQcmDto> QuestionQcmDtos = questionQcmService.getQuestionQcm(id);
        return new ResponseEntity<>(QuestionQcmDtos, HttpStatus.OK);
    }

    @GetMapping("/get-by-entretien/{id}")
    public ResponseEntity<List<QuestionQcmDto>> getQcmQuestionOfQcmbyEntretien(@PathVariable("id") Long id) {
        Optional<Entretien> entretienOpt = entretienRepository.findById(id);
        if(entretienOpt.isPresent()){
            Entretien entretien = entretienOpt.get();
            List<QuestionQcmDto> QuestionQcmDtos = questionQcmService.getQuestionQcm(entretien.getQcm().getId());
            return new ResponseEntity<>(QuestionQcmDtos, HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/update-order")
    public ResponseEntity<List<QuestionQcmDto>> updateOrder(@RequestBody List<QuestionQcmDto> questionQcmDtos) {
        List<QuestionQcmDto> updatedQuestions  = new ArrayList<>();
        for(QuestionQcmDto questionQcmDto: questionQcmDtos) {
            QuestionQcmDto updatedQuestion = questionQcmService.updateOrder(questionQcmDto);
            updatedQuestions.add(updatedQuestion);
        }
        return new ResponseEntity<>(updatedQuestions, HttpStatus.OK);
    }
}
