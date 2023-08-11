package fr.mipih.rh.testcandidats.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	// Chercher un administrateur par son ID
	Optional<Admin> findById(Long id);

	// Chercher un administrateur par son nom
	Optional<Admin> findByNom(String nom);

	// Chercher un administrateur par son nom et prénom
	Optional<Admin> findByNomAndPrenom(String nom, String prenom);
	
	Optional<Admin> findByRefreshToken(String refreshToken);
}
