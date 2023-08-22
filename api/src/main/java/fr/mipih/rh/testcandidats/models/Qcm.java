package fr.mipih.rh.testcandidats.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QCM")
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
	private Set<Entretien> entretiens = new HashSet<>();

	@OneToMany(mappedBy = "qcm", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<QuestionQcm> questions = new HashSet<>();

	@Override
	public int hashCode() {
		return Objects.hash(id, nom, temps, point);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Qcm other = (Qcm) obj;
		return Objects.equals(id, other.id);
	}
}
