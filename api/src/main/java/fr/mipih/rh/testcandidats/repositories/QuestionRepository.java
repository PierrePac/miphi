package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
