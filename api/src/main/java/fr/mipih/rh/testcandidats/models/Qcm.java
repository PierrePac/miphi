package fr.mipih.rh.testcandidats.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QCM")
@Data
@EqualsAndHashCode(exclude = "questions")

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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "qcm_id", referencedColumnName = "id")
	Set<Entretien> entretiens = new HashSet<>();
	
	@ManyToMany(mappedBy = "qcms")
	@JsonIgnore
	Set<Question> questions = new HashSet<>();

}
