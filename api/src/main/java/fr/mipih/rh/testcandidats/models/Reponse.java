package fr.mipih.rh.testcandidats.models;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPONSE")
@Data
public class Reponse {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "reponse")
	private String reponse;
	
	@Column(name = "correct")
	private boolean correct;
	
	@Column(name = "question_id")
	
	@ManyToMany(mappedBy = "reponses")
	@JsonIgnore
	Set<ReponseCandidat> reponseCandidats = new HashSet<>();

}
