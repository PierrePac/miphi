package fr.mipih.rh.testcandidats.repositories;

import fr.mipih.rh.testcandidats.models.QuestionQcm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionQcmRepository extends JpaRepository<QuestionQcm, Long> {
	
}
