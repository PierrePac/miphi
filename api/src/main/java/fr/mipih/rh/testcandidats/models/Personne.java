package fr.mipih.rh.testcandidats.models;

import fr.mipih.rh.testcandidats.models.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERSONNE")
@Data
public class Personne {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "prenom")
	private String prenom;
	
	@Column(name = "motDePasse")
	private String motDePasse;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;
	
	@OneToOne(mappedBy = "personne", cascade = CascadeType.PERSIST)
	private Admin admin;
	
	@OneToOne(mappedBy = "personne", cascade = CascadeType.PERSIST)
	private Candidat candidat;

	
}
