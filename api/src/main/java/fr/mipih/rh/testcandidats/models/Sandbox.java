package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Set;

import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SANDBOX")
@Data
@ToString(exclude = {"entretiens"})
public class Sandbox {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "scr")
	private String src;

	@Column(name = "nom")
	private String nom;

	@Column(name = "consigne", length = 2000)
	private String consigne;

	@Enumerated(EnumType.STRING)
	@Column(name = "niveau")
	private Niveau niveau;

	@Enumerated(EnumType.STRING)
	@Column(name = "technologie")
	private Technologie technologie;

	@OneToMany(mappedBy = "sandbox", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Entretien> entretiens = new HashSet<>();

}
