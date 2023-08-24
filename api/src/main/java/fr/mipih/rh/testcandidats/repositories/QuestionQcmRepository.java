package fr.mipih.rh.testcandidats.repositories;

import fr.mipih.rh.testcandidats.models.QuestionQcm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionQcmRepository extends JpaRepository<QuestionQcm, Long> {

    Optional<List<QuestionQcm>> findAllByQcmId(Long qcmId);
	
}
