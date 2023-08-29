package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CANDIDAT")
@Table(name = "CANDIDAT")
@Getter
@Setter
@SuperBuilder
@ToString(exclude = {"entretien", "reponseCandidats"})
public class Candidat extends Personne {


	@ManyToOne( targetEntity = Entretien.class, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="integer", name = "entretien_id")
	private Entretien entretien;

	@OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ReponseCandidat> reponseCandidats;

}

