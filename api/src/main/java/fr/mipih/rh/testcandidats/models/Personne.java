package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERSONNE")
@Data
@ToString(exclude={"admin", "candidat", "role"})
public class Personne {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "prenom")
	private String prenom;
	
	private boolean enabled;
	
	@OneToOne(mappedBy = "personne", cascade = CascadeType.PERSIST)
	@EqualsAndHashCode.Exclude
	private Admin admin;
	
	@OneToOne(mappedBy = "personne", cascade = CascadeType.PERSIST)
	@EqualsAndHashCode.Exclude
	private Candidat candidat;

	@ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id") 
    private Role role;
}
