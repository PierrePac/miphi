package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import fr.mipih.rh.testcandidats.models.enums.Categorie;
import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QUESTION")
@ToString(exclude = {"questionQcms", "propositions"})
public class Question {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@Generated(GenerationTime.INSERT)
	private Long id;

	@Column(name = "question", columnDefinition = "TEXT")
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
	private Set<QuestionQcm> questionQcms;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "question")
	Set<Proposition> propositions;


}
