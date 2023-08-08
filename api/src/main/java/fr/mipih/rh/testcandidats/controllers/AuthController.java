package fr.mipih.rh.testcandidats.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.config.UserAuthProvider;
import fr.mipih.rh.testcandidats.dtos.CredentialsAdminDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.dtos.PersonneDto;
import fr.mipih.rh.testcandidats.services.PersonneService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
	
	private final PersonneService personneService;
	private final UserAuthProvider userAuthProvider;
	
	
	@PostMapping("/admin")
	public ResponseEntity<PersonneDto> loginAdmin(@RequestBody CredentialsAdminDto credentialsDto){
		PersonneDto admin = personneService.loginAdmin(credentialsDto);
		admin.setToken(userAuthProvider.createToken(admin));
		return ResponseEntity.ok(admin);
		
	}
	
	@PostMapping("/candidat")
	public ResponseEntity<PersonneDto> loginCandidat(@RequestBody  CredentialsCandidatDto credentialsDto){
		PersonneDto candidat = personneService.loginCandidat(credentialsDto);
		candidat.setToken(userAuthProvider.createToken(candidat));
		return ResponseEntity.ok(candidat);
	}
	
	@PostMapping("/add-admin")
	public ResponseEntity<PersonneDto> ajoutAdmin(@RequestBody NewAdminDto newAdminDto) {
		PersonneDto admin = personneService.ajoutAdmin(newAdminDto);
		return ResponseEntity.created(URI.create("/personne/" + admin.getId())).body(admin);
	}
	
}
