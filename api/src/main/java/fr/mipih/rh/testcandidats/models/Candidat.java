package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CANDIDAT")
@Table(name = "CANDIDAT")
@Data
@SuperBuilder
@ToString
public class Candidat extends Personne {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entretien_id")
	private Entretien entretien;

	@OneToMany(mappedBy = "candidat", fetch = FetchType.EAGER)
	private Set<ReponseCandidat> reponseCandidats;

	public Candidat() {}
}
