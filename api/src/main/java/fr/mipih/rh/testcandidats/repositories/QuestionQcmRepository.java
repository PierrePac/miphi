package fr.mipih.rh.testcandidats.repositories;

import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import jakarta.transaction.Transactional;

@Repository
public interface QuestionQcmRepository extends JpaRepository<QuestionQcm, QuestionQcmId> {

    List<QuestionQcm> findAllByQuestionQcmIdIdQcm(Long idQcm);

}
