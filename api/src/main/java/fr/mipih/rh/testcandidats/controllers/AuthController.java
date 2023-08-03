package fr.mipih.rh.testcandidats.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.mipih.rh.testcandidats.dtos.AdminDTO;
import fr.mipih.rh.testcandidats.dtos.CandidatDTO;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
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
			Admin admin = authService.adminInfo(nom);
			AdminDTO adminDto = AdminDTO.builder()
					.id(admin.getPersonne().getId())
					.nom(admin.getPersonne().getNom())
					.prenom(admin.getPersonne().getPrenom())
					.role(admin.getPersonne().getRoles().iterator().next().getName()) 
					.build();
			return new ResponseEntity<>(adminDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/candidat")
	public ResponseEntity<?> loginCandidat(@RequestBody Map<String, String> loginInfo){
		String nom = loginInfo.get("nom");
		String prenom = loginInfo.get("prenom");
		ConnexionStatus status = authService.verifierCandidat(nom, prenom);
		if(status.equals(ConnexionStatus.CANDIDAT)) {
			Candidat candidat = authService.candidatInfo(nom);
			CandidatDTO candidatDto = CandidatDTO.builder()
					.id(candidat.getPersonne().getId())
					.nom(candidat.getPersonne().getNom())
					.prenom(candidat.getPersonne().getPrenom())
					.role(candidat.getPersonne().getRoles().iterator().next().getName())
					.test_id(candidat.getPersonne().getCandidat().getTestId()) 
					.build();
			return new ResponseEntity<>(candidatDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
		}
	}
	
}
