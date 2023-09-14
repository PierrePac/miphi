package fr.mipih.rh.testcandidats.models;


import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("ADMIN")
//@Table(name = "ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"entretiens"})
@EqualsAndHashCode(callSuper = false)
public class Admin extends Personne {

	@Column(name = "motDePasse")
	private String motDePasse;

	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Entretien> entretiens;

}

