package fr.mipih.rh.testcandidats.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "ENTRETIEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@ToString(exclude = {"candidats", "admin", "qcm", "sandbox"})
public class Entretien {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "dateStart")
	private Date dateStart;

	@Column(name = "dateEnd")
	private Date dateEnd;

	@JsonManagedReference
	@OneToMany(mappedBy = "entretien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Candidat> candidats = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = true)
	private Admin admin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qcm_id")
	private Qcm qcm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sandbox_id")
	private Sandbox sandbox;


}
