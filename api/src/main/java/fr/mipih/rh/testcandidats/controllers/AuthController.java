package fr.mipih.rh.testcandidats.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.services.AuthService;

@Controller
@RequestMapping("/login")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/admin")
	public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginInfo){
		String nom = loginInfo.get("nom");
		String motDePasse = loginInfo.get("motDePasse");
		ConnexionStatus status = authService.verifierMotDePasseAdmin(nom, motDePasse);
		if(status.equals(ConnexionStatus.ADMIN)) {
			return new ResponseEntity<>(status, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/candidat")
	public ResponseEntity<ConnexionStatus> loginCandidat(@RequestBody Map<String, String> loginInfo){
		String nom = loginInfo.get("nom");
		String prenom = loginInfo.get("prenom");
		ConnexionStatus status = authService.verifierCandidat(nom, prenom);
		if(status.equals(ConnexionStatus.CANDIDAT)) {
			return new ResponseEntity<>(status, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
		}
	}
}
