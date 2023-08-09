package fr.mipih.rh.testcandidats.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {

	// Chercher un administrateur par son ID
	Optional<Candidat> findById(Long id);

	// Chercher un administrateur par son nom
	Optional<Candidat> findByNom(String nom);

	// Chercher un administrateur par son nom et prénom
	Optional<Candidat> findByNomAndPrenom(String nom, String prenom);
	
	Optional<Candidat> findByRefreshToken(String refreshToken);
}
