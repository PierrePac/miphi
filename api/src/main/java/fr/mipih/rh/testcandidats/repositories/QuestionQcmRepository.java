package fr.mipih.rh.testcandidats.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import jakarta.transaction.Transactional;

@Repository
public interface QuestionQcmRepository extends JpaRepository<QuestionQcm, Long> {

	@Transactional
    Optional<List<QuestionQcm>> findAllByQcmId(Long qcm_id);
    
    Optional<List<QuestionQcm>> findAllByQcm(QcmDto qcm);
	
}
