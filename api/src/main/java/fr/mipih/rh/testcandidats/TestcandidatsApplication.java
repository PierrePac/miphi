package fr.mipih.rh.testcandidats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.mipih.rh.testcandidats.repositories")
@ComponentScan("fr.mipih.rh.testcandidats.security")
@ComponentScan("fr.mipih.rh.testcandidats.controllers")
@EntityScan("fr.mipih.rh.testcandidats.models")
@EnableScheduling
public class TestcandidatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestcandidatsApplication.class, args);
	}
	


}
