package fr.mipih.rh.testcandidats.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "CANDIDAT")
@Data
@ToString(exclude="personne")
public class Candidat {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "personne_id", referencedColumnName = "id")
	@EqualsAndHashCode.Exclude
	Personne personne;
	
	@Column(name = "test_id")
	Long testId;
	
	@OneToMany(mappedBy = "candidat", fetch = FetchType.EAGER)
	private Set<ReponseCandidat> reponseCandidats;


}
