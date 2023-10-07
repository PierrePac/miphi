package fr.mipih.rh.testcandidats.models;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QCM")
@ToString(exclude = {"entretiens", "questionsQcm"})
public class Qcm {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nom")
	private String nom;

	@Column(name = "temps")
	private int temps;

	@Column(name = "point")
	private int point;

	@OneToMany(mappedBy = "qcm", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Entretien> entretiens;

	@OneToMany(mappedBy = "qcm", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<QuestionQcm> questionsQcm;
}
