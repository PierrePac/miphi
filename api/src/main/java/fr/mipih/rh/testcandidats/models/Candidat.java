package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

	@Column(name = "entretien_id")
	Long entretienId;

	@OneToMany(mappedBy = "candidat", fetch = FetchType.EAGER)
	private Set<ReponseCandidat> reponseCandidats;

	public Candidat() {}
}
