package fr.mipih.rh.testcandidats.services;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.CredentialsAdminDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.dtos.PersonneDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.AdminMapper;
import fr.mipih.rh.testcandidats.mappers.PersonneMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.enums.Role;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonneService {

	private final PersonneRepository personneRepository;
	private final PasswordEncoder passwordEncoder;
	private final PersonneMapper personneMapper;
	private final AdminMapper adminMapper;
	private final AdminRepository adminRepository;
	
	public PersonneDto loginAdmin(CredentialsAdminDto credentialsDto) {
		Personne admin = personneRepository.findByNom(credentialsDto.nom())
			.orElseThrow(() -> new AppException("Personne Inconnu", HttpStatus.NOT_FOUND));
		
		if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.motDePasse()), admin.getAdmin().getMotDePasse())) {
			return personneMapper.toPersonneDto(admin);
		}
		throw new AppException("Mot de passe incorrect", HttpStatus.BAD_REQUEST);
	}
	
	public PersonneDto loginCandidat(CredentialsCandidatDto credentialsDto) {
		Personne candidat = personneRepository.findByNom(credentialsDto.nom())
				.orElseThrow(() -> new AppException("Personne Inconnu", HttpStatus.NOT_FOUND));
		if(candidat.getPrenom().equals(credentialsDto.prenom())) {
			if(candidat.getCandidat().getTestId() != null) {
				return personneMapper.toPersonneDto(candidat);
			}
			throw new AppException("Pas de test associé à ce candidat", HttpStatus.BAD_REQUEST);
		}
		throw new AppException("Candidat inconnu", HttpStatus.BAD_REQUEST);
	}
	
	public PersonneDto ajoutAdmin(NewAdminDto newAdminDto) {
		Optional<Personne> p = personneRepository.findByNomAndPrenom(newAdminDto.nom(), newAdminDto.prenom());
		
		if (p.isPresent()) {
			throw new AppException("Admin déjà existant", HttpStatus.BAD_REQUEST);
		}
		
		Personne personne = personneMapper.ajouterAdmin(newAdminDto);
		personne.setRole(Role.ADMIN);
		
		Admin admin = adminMapper.ajouterAdmin(newAdminDto);
		admin.setPersonne(personne);
		admin.setMotDePasse(passwordEncoder.encode(CharBuffer.wrap(newAdminDto.motDePasse())));
		
		Personne enregistrerPresonne = personneRepository.save(personne);
		adminRepository.save(admin);
		return personneMapper.toPersonneDto(enregistrerPresonne);
	}
	
	public PersonneDto findByNom(String nom) {
        Personne user = personneRepository.findByNom(nom)
                .orElseThrow(() -> new AppException("personne inconnu", HttpStatus.NOT_FOUND));
        return personneMapper.toPersonneDto(user);
    }
	
}



























