package fr.mipih.rh.testcandidats.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Personne;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {

	Optional<Personne> findByNom(String nom);
	
	Personne findByAdminIsNotNull();
	
	Personne findByCandidatIsNotNull();

	Optional<Personne> findByNomAndPrenom(String nom, String prenom);

}
