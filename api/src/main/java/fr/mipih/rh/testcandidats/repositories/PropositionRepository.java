package fr.mipih.rh.testcandidats.repositories;

import java.util.List;

import fr.mipih.rh.testcandidats.models.Proposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PropositionRepository extends JpaRepository<Proposition, Long> {
	
	List<Proposition> findAllByQuestionId(Long questionId);

}
