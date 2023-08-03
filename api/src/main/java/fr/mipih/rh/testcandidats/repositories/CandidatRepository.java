package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {

	Candidat findByPersonne(Personne personne);
}
