package fr.mipih.rh.testcandidats.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.services.SandboxService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sandbox")
@RequiredArgsConstructor
public class SandboxController {

    private final SandboxService sandboxService;

    @GetMapping("/get/{id}")
    public ResponseEntity<SandboxDto> getSandBox(@PathVariable Long id) {
        SandboxDto sandboxDto = sandboxService.findById(id);
        return ResponseEntity.ok(sandboxDto);
    }

    @PostMapping("/add")
    public ResponseEntity<SandboxDto> createSandbox(@RequestBody SandboxDto sandboxDto) {
        SandboxDto savedSandbox = sandboxService.save(sandboxDto);
        return new ResponseEntity<>(savedSandbox, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SandboxDto>> getAllSandbox() {
        List<SandboxDto> sandboxDtoList = sandboxService.getAll();
        return new ResponseEntity<List<SandboxDto>>(sandboxDtoList, HttpStatus.OK);
    }

    @GetMapping("/get-consignes/{idEntretien}")
    public ResponseEntity<Map<String, String>> getConsignes(@PathVariable Long idEntretien) {
        Map<String, String> reponse = new HashMap<>();
        reponse.put("consignes", sandboxService.getConsignes(idEntretien));
        return ResponseEntity.ok(reponse);
    }

}
