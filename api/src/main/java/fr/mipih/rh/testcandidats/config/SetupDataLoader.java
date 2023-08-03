package fr.mipih.rh.testcandidats.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Personne;
import fr.mipih.rh.testcandidats.models.Privilege;
import fr.mipih.rh.testcandidats.models.Role;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PersonneRepository;
import fr.mipih.rh.testcandidats.repositories.PrivilegeRepository;
import fr.mipih.rh.testcandidats.repositories.RoleRepository;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private PersonneRepository personneRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private CandidatRepository candidatRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;

		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

		List<Privilege> personnePrivileges = Arrays.asList(readPrivilege, writePrivilege);
		Role adminRole = createRoleIfNotFound("ROLE_ADMIN", personnePrivileges);
		Role candidateRole = createRoleIfNotFound("ROLE_CANDIDAT", personnePrivileges);

		Personne existingAdminPersonne = personneRepository.findByNomAndPrenom("pac", "pierre");
		if (existingAdminPersonne == null) {
			Personne adminPersonne = new Personne();
			adminPersonne.setNom("pac");
			adminPersonne.setPrenom("pierre");
			adminPersonne.setEnabled(true);
			adminPersonne.getRoles().add(adminRole);
			Admin admin = new Admin();
			admin.setMotDePasse(passwordEncoder.encode("admin"));
			admin.setPersonne(adminPersonne);

			adminPersonne.setAdmin(admin);

			adminRepository.save(admin);
			personneRepository.save(adminPersonne);
		}

		Personne existingCandidatPersonne = personneRepository.findByNomAndPrenom("dupond", "toto");
		if (existingCandidatPersonne == null) {
			Personne candidatPersonne = new Personne();
			candidatPersonne.setNom("dupond");
			candidatPersonne.setPrenom("toto");
			candidatPersonne.setEnabled(true);
			candidatPersonne.getRoles().add(candidateRole);

			Candidat candidat = new Candidat();
			candidat.setPersonne(candidatPersonne);

			candidatPersonne.setCandidat(candidat);

			candidatRepository.save(candidat);
			personneRepository.save(candidatPersonne);
		}

	}

	@Transactional
	Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role();
			role.setName(name);
			role.setPrivileges(new HashSet<>(privileges));
			roleRepository.save(role);
		}
		return role;
	}
}
