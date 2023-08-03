package fr.mipih.rh.testcandidats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mipih.rh.testcandidats.models.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	
	Privilege findByName(String name);

}
