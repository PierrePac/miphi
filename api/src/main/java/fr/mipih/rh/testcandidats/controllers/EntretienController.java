package fr.mipih.rh.testcandidats.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.services.EntretienService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entretien")
@RequiredArgsConstructor
public class EntretienController {

    private final EntretienService entretienService;

    @GetMapping("/get/{id}")
    public ResponseEntity<EntretienDto> getEntretien(@PathVariable Long id) {
        EntretienDto entretiendto = entretienService.findById(id);
        return ResponseEntity.ok(entretiendto);
    }

    @PostMapping("/add")
    public ResponseEntity<EntretienDto> createEntretien(@RequestBody EntretienDto entretienDto) {
        EntretienDto savedEntretien = entretienService.save(entretienDto);
        return new ResponseEntity<>(savedEntretien, HttpStatus.CREATED);
    }

}
