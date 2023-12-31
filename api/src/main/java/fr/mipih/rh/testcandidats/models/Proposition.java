package fr.mipih.rh.testcandidats.models;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROPOSITIONS")
@Data
@EqualsAndHashCode(exclude = {"reponseCandidats"})
@ToString(exclude = {"question", "reponseCandidats"})
public class Proposition {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "reponse")
	private String reponse;
	
	@Column(name = "correct")
	private boolean correct;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;

	@OneToMany(mappedBy = "proposition", fetch = FetchType.EAGER)
	private Set<ReponseCandidat> reponseCandidats;


}
