package fr.mipih.rh.testcandidats.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "ENTRETIEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@ToString(exclude = {"candidats", "qcm", "admin", "sandbox"})
public class Entretien {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nom")
	private String nom;

	@Column(name = "dateStart")
	private Date dateStart;

	@Column(name = "dateEnd")
	private Date dateEnd;

	@JsonManagedReference
	@OneToMany(mappedBy = "entretien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Candidat> candidats = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qcm_id")
	private Qcm qcm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sandbox_id")
	private Sandbox sandbox;

	@ManyToOne( targetEntity = Admin.class,
			fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "admin_id")
	private Admin admin;

}
