package fr.mipih.rh.testcandidats.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.mipih.rh.testcandidats.config.UserAuthProvider;
import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsAdminDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.dtos.RefreshTokenRequestDto;
import fr.mipih.rh.testcandidats.services.AdminService;
import fr.mipih.rh.testcandidats.services.CandidatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
	
	private final AdminService adminService;
	private final CandidatService candidatService;
	private final UserAuthProvider userAuthProvider;
	
	
	@PostMapping("/admin")
	public ResponseEntity<AdminDto> loginAdmin(@RequestBody CredentialsAdminDto credentialsDto){
		AdminDto admin = adminService.loginAdmin(credentialsDto);
		Map<String, String> tokens = userAuthProvider.createTokens(admin);
		admin.setToken(tokens.get("accessToken"));
		admin.setRole("ADMIN");
		return ResponseEntity.ok(admin);
		
	}
	
	@PostMapping("/candidat")
	public ResponseEntity<CandidatDto> loginCandidat(@RequestBody  CredentialsCandidatDto credentialsDto){
		CandidatDto candidat = candidatService.loginCandidat(credentialsDto);
		Map<String, String> tokens = userAuthProvider.createTokens(candidat);
		candidat.setToken(tokens.get("accessToken"));
		candidat.setRole("CANDIDAT");
		return ResponseEntity.ok(candidat);
	}
	
	@PostMapping("/add-admin")
	public ResponseEntity<AdminDto> ajoutAdmin(@RequestBody NewAdminDto newAdminDto) {
		AdminDto admin = adminService.ajoutAdmin(newAdminDto);
		return ResponseEntity.created(URI.create("/personne/" + admin.getId())).body(admin);
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequestDto request) {
		String refreshToken = request.getRefreshToken();
		if(refreshToken == null || refreshToken.isEmpty()) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Refresh token est manquant"));
		}
		
		String newAuthToken;
		try {
			newAuthToken = userAuthProvider.refreshAccessToken(refreshToken);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Refresh token invalid"));
		}
		
		Map<String, String> reponse = new HashMap<>();
		reponse.put("token", newAuthToken);
		return ResponseEntity.ok(reponse);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody Map<String, String> tokens) {
		String refreshToken = tokens.get("refreshToken");
		AdminDto admin = adminService.findByRefreshToken(refreshToken);
		CandidatDto candidat = candidatService.findByRefreshToken(refreshToken);
		
		if(admin != null) {
			adminService.clearTokens(admin);
			return ResponseEntity.ok().body("Admin déconnecté avec succès");
		} else if (candidat != null) {
			candidatService.clearTokens(candidat);
			return ResponseEntity.ok().body("Candidat déconnecté avec succès");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalid");
		}
		
	}
}
