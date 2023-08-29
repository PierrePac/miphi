package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "entretien", nullable = true)
	@NotFound( action = NotFoundAction.IGNORE )
	private Entretien entretien = null;

	@OneToMany(mappedBy = "candidat", fetch = FetchType.LAZY)
	private Set<ReponseCandidat> reponseCandidats;

}

