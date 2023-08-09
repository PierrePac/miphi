package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Set;

import fr.mipih.rh.testcandidats.models.enums.Categorie;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "QUESTION")
@Data
public class Question {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "question_qcm",
			joinColumns = @JoinColumn(
				name = "question_id", referencedColumnName = "id"
			),
			inverseJoinColumns = @JoinColumn(
				name = "qcm_id", referencedColumnName = "id"
			)
	)
	Set<Qcm> qcms = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	Set<Reponse> reponses = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	Set<ReponseCandidat> reponseCandidats = new HashSet<>();
	
}
