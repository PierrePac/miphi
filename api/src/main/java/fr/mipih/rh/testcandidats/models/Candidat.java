package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CANDIDAT")
@Table(name = "CANDIDAT")
@Data
@SuperBuilder
@EqualsAndHashCode(exclude = {"entretien", "reponseCandidats"})
@ToString(exclude = {"entretien", "reponseCandidats"})
public class Candidat extends Personne {

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entretien_id")
	private Entretien entretien;

	@OneToMany(mappedBy = "candidat", fetch = FetchType.EAGER)
	private Set<ReponseCandidat> reponseCandidats;

	public Candidat() {}
}
