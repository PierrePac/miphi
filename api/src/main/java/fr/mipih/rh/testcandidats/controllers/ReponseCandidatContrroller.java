package fr.mipih.rh.testcandidats.controllers;

import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.services.ReponseCandidatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reponse-candidat")
@RequiredArgsConstructor
public class ReponseCandidatContrroller {

    private final ReponseCandidatService reponseCandidatService;

    @PostMapping("/saveAll")
    public ResponseEntity<Map<String, String>> saveAll(@RequestBody List<ReponseCandidatDto> reponseCandidatDtoList) {
        Map<String, String> response = new HashMap<>();

        try {
            reponseCandidatService.saveAll(reponseCandidatDtoList);
            response.put("message", "Les réponses ont été enregistrées avec succès.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Une erreur est survenue lors de l'enregistrement des réponses.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-by-candidat/{id}")
    public ResponseEntity<List<ReponseCandidatDto>> getResponseCandidatByCandidat(@PathVariable("id") Long id) {
        List<ReponseCandidatDto> reponseCandidatDtoList = reponseCandidatService.getResponseCandidatByCandidat(id);
        return new ResponseEntity<>(reponseCandidatDtoList, HttpStatus.OK);
    }
}
