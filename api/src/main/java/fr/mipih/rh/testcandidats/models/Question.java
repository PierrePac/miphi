package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import fr.mipih.rh.testcandidats.models.enums.Categorie;
import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "QUESTION")
public class Question {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@Generated(GenerationTime.INSERT)
	private Long id;

	@Column(name = "question")
	private String question;

	@Column(name = "point")
	private int point;

	@Column(name = "temps")
	private int temps;

	@Enumerated(EnumType.STRING)
	@Column(name = "categorie")
	private Categorie categorie;

	@Enumerated(EnumType.STRING)
	@Column(name = "niveau")
	private Niveau niveau;

	@Enumerated(EnumType.STRING)
	@Column(name = "technologie")
	private Technologie technologie;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<QuestionQcm> questionQcms = new HashSet<>();


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "question")
	Set<Reponse> reponses = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	Set<ReponseCandidat> reponseCandidats = new HashSet<>();

	@Override
	public int hashCode() {
		return Objects.hash(id, question, point, temps, categorie, niveau, technologie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return Objects.equals(id, other.id);
	}

}
