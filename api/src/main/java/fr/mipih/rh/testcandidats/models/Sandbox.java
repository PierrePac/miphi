package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Set;

import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SANDBOX")
@Data
public class Sandbox {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "scr")
	private String src;

	@Enumerated(EnumType.STRING)
	@Column(name = "niveau")
	private Niveau niveau;

	@Enumerated(EnumType.STRING)
	@Column(name = "technologie")
	private Technologie technologie;

	@OneToMany(mappedBy = "sandbox", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Entretien> entretiens = new HashSet<>();

}
