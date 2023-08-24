package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@DiscriminatorValue("CANDIDAT")
@Table(name = "CANDIDAT")
@Getter
@Setter
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
