package fr.mipih.rh.testcandidats.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.config.UserAuthProvider;
import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.dtos.NewCandidatDto;
import fr.mipih.rh.testcandidats.services.AdminService;
import fr.mipih.rh.testcandidats.services.CandidatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/personne")
@RequiredArgsConstructor
public class PersonneController {

    private final AdminService adminService;
    private final CandidatService candidatService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/add-admin")
    public ResponseEntity<AdminDto> ajoutAdmin(@RequestBody NewAdminDto newAdminDto) {
        AdminDto admin = adminService.ajoutAdmin(newAdminDto);
        return ResponseEntity.created(URI.create("/personne/" + admin.getId())).body(admin);
    }

    @PostMapping("/add-candidat")
    public ResponseEntity<CandidatDto> ajoutCandidat(@RequestBody NewCandidatDto newCandidatDto) {
        CandidatDto candidatDto = candidatService.ajoutCandidat(newCandidatDto);
        return ResponseEntity.created(URI.create("/personne/" + candidatDto.getId())).body(candidatDto);
    }


}
