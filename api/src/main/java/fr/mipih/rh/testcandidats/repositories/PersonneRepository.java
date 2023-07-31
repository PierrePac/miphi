package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Personne;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {

	Personne findByNom(String nom);
	
	@Query("SELECT p.motDePasse FROM Personne p WHERE p.nom = ?1")
	String findMotDePasseByNom(String nom);
	
	Personne findByAdminIsNotNull();
	
	Personne findByCandidatIsNotNull();
}
