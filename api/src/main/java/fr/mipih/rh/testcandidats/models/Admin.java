package fr.mipih.rh.testcandidats.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "ADMIN")
@Data
@SuperBuilder
@ToString
public class Admin extends Personne {
	
	@Column(name = "motDePasse")
	private String motDePasse;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "test_id", referencedColumnName = "id")
	Set<Entretien> entretiens = new HashSet<>();

	public Admin() {}
}
