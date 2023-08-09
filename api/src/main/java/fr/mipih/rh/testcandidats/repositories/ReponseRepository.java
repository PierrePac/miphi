package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Reponse;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {

}
