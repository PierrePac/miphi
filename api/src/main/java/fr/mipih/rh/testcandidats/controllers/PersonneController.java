package fr.mipih.rh.testcandidats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mipih.rh.testcandidats.dtos.PersonneDTO;
import fr.mipih.rh.testcandidats.models.enums.ConnexionStatus;
import fr.mipih.rh.testcandidats.services.AuthService;
import fr.mipih.rh.testcandidats.services.PersonneService;

@Controller
@RequestMapping("/api/personnes")
public class PersonneController {

	private final PersonneService personneService;
	private final AuthService authService;
	
	@Autowired
	public PersonneController(PersonneService personneService, AuthService authService) {
		this.personneService = personneService;
		this.authService = authService;
	}
	
	@PostMapping("/connexion")
	@ResponseBody
	public ResponseEntity<ConnexionStatus> verifierNomMotDePasse(@RequestBody PersonneDTO personneDTO) {
		String nom = personneDTO.getNom();
		String motDePasse = personneDTO.getMotDePasse();
		
		ConnexionStatus status = authService.verifierMotDePasseAdmin(nom, motDePasse);
		return ResponseEntity.ok(status);
	}
	
	
	@PostMapping("/enregistrer")
	@ResponseBody
	public ResponseEntity<String> enregistrerPersonne(@RequestBody PersonneDTO personneDTO) {
		boolean enregistrementReussi = personneService.enregistrerPersonne(personneDTO);
		if (enregistrementReussi) {
			return ResponseEntity.ok("Personne enregistrée avec succès.");
		} else {
			return ResponseEntity.badRequest().body("Echec de l'enregistrement de la personne.");
		}
	}
	
}
