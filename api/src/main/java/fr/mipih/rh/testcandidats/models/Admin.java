package fr.mipih.rh.testcandidats.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class Admin extends Personne {

	@Column(name = "motDePasse")
	private String motDePasse;

}

