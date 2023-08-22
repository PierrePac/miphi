package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Sandbox;

import java.util.Optional;

@Repository
public interface SandboxRepository extends JpaRepository<Sandbox, Long> {

    Optional<Sandbox> findById(Long id);

}
