package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "REPONSECANDIDAT")
@Data
public class ReponseCandidat {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "candidat_id", referencedColumnName = "id")
	private Candidat candidat;
	
	@ManyToOne
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	private Question question;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name = "reponseCandidat_reponse",
		joinColumns = @JoinColumn(
			name = "reponseCandidat_id", referencedColumnName = "id"
		),
		inverseJoinColumns = @JoinColumn(
			name = "reponse_id", referencedColumnName = "id"
		)
	)
	Set<Reponse> reponses = new HashSet<>();

}
