package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.mipih.rh.testcandidats.models.Entretien;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Long> {

    Long findById(Optional<Entretien> entretien);

    List<Entretien> findByDateEndBefore(Date now);
}
