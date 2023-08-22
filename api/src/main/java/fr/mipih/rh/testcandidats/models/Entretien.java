package fr.mipih.rh.testcandidats.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
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
@Table(name = "ENTRETIEN")
@Data
public class Entretien {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "dateStart")
	private Date dateStart;

	@Column(name = "dateEnd")
	private Date dateEnd;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "entretien_id", referencedColumnName = "id")
	Set<Candidat> candidats = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qcm_id")
	private Qcm qcm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sandbox_id")
	private Sandbox sandbox;


}
