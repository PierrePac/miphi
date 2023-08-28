package fr.mipih.rh.testcandidats.repositories;

import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.models.ReponseCandidatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReponseCandidatRepository extends JpaRepository<ReponseCandidat, ReponseCandidatId> {
}
