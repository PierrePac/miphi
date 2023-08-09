package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.mipih.rh.testcandidats.models.Entretien;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {

}
