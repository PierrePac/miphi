package fr.mipih.rh.testcandidats.repositories;

import java.util.List;
import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.mipih.rh.testcandidats.models.QuestionQcm;

@Repository
public interface QuestionQcmRepository extends JpaRepository<QuestionQcm, QuestionQcmId> {

    List<QuestionQcm> findAllByQuestionQcmIdIdQcm(Long idQcm);

    void deleteByQuestionQcmId_IdQuestion(Long idQuestion);

    List<QuestionQcm> findByQuestionQcmId_IdQuestion(Long idQuestion);
}
