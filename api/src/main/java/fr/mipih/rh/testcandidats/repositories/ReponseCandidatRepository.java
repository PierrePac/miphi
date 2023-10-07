package fr.mipih.rh.testcandidats.repositories;

import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.models.ReponseCandidatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReponseCandidatRepository extends JpaRepository<ReponseCandidat, ReponseCandidatId> {

    List<ReponseCandidat> findAllByReponseCandidatIdIdCandidat(Long idCandidat);

    void deleteByReponseCandidatId_IdProposition(Long idProposition);

    Optional<ReponseCandidat> findByReponseCandidatId_IdProposition(Long idProposition);
}
