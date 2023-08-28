package fr.mipih.rh.testcandidats.services;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsAdminDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.AdminMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final PasswordEncoder passwordEncoder;
	private final AdminMapper adminMapper;
	private final AdminRepository adminRepository;
	
	public AdminDto loginAdmin(CredentialsAdminDto credentialsDto) {
		Admin admin = adminRepository.findByNom(credentialsDto.nom())
				.orElseThrow(() -> new AppException("Admin inconnu", HttpStatus.NOT_FOUND));
		
		if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.motDePasse()), admin.getMotDePasse())) {
			return adminMapper.toAdminDto(admin);
		}
		throw new AppException("Mot de passe incorrect", HttpStatus.BAD_REQUEST);
	}
	
	public AdminDto ajoutAdmin(NewAdminDto newAdminDto) {
		Optional<Admin> doublon = adminRepository.findByNomAndPrenom(newAdminDto.getNom(), newAdminDto.getPrenom());
		if(doublon.isPresent()) {
			throw new AppException("Admin déjà présent", HttpStatus.BAD_REQUEST);
		}
		
		Admin admin = adminMapper.ajouterAdmin(newAdminDto);
		admin.setNom(newAdminDto.getNom());
		admin.setPrenom(newAdminDto.getPrenom());
		admin.setMotDePasse(passwordEncoder.encode(CharBuffer.wrap(newAdminDto.getMotDePasse())));

		adminRepository.save(admin);
		return adminMapper.toAdminDto(admin);
	}
	
	public AdminDto findByNom(String nom) {
		Admin admin = adminRepository.findByNom(nom)
				.orElseThrow(() -> new AppException("Admin inconnu", HttpStatus.NOT_FOUND));
		return adminMapper.toAdminDto(admin);
	}
	
	public AdminDto findByRefreshToken(String refreshToken) {
	    Admin admin = adminRepository.findByRefreshToken(refreshToken)
	            .orElseThrow(() -> new AppException("Admin avec ce refreshToken inconnu", HttpStatus.NOT_FOUND));
	    return adminMapper.toAdminDto(admin);
	}
	
	public AdminDto save(AdminDto adminDto) {
		Admin existingAdmin = adminRepository.findByNom(adminDto.getNom())
				.orElseThrow(() -> new AppException("Admin inconnu", HttpStatus.NOT_FOUND));
		existingAdmin.setToken(adminDto.getToken());
		existingAdmin.setRefreshToken(adminDto.getRefreshToken());
		
        Admin updatedAdmin = adminRepository.save(existingAdmin); 
      
        return adminMapper.toAdminDto(updatedAdmin);
    }
	
	public void clearTokens(AdminDto adminDto) {
		Admin existingAdmin = adminRepository.findByNom(adminDto.getNom())
				.orElseThrow(() -> new AppException("Admin inconnu", HttpStatus.NOT_FOUND));
		existingAdmin.setToken(null);
		existingAdmin.setRefreshToken(null);
		
        adminRepository.save(existingAdmin); 
	}
}
