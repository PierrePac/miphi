package fr.mipih.rh.testcandidats.controllers;

import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.services.ReponseCandidatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reponse-candidat")
@RequiredArgsConstructor
public class ReponseCandidatContrroller {

    private final ReponseCandidatService reponseCandidatService;

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody List<ReponseCandidatDto> reponseCandidatDtoList) {
        try {
            reponseCandidatService.saveAll(reponseCandidatDtoList);  // Assurez-vous que reponseCandidatService est injecté
            return new ResponseEntity<>("Les réponses ont été enregistrées avec succès.", HttpStatus.CREATED);
        } catch (Exception e) {
            // Loggez l'exception pour le débogage
            return new ResponseEntity<>("Une erreur est survenue lors de l'enregistrement des réponses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
