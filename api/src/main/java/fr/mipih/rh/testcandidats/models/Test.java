package fr.mipih.rh.testcandidats.models;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "TEST")
@Data
public class Test {

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "dateStart")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateStart;
	
	@Column(name = "dateEnd")
	private Date dateEnd;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "test_id", referencedColumnName = "id")
	Set<Candidat> candidats = new HashSet<>();
	
	@Column(name = "admin_id")
	Long adminId;
	
	@Column(name = "qcm_id")
	Long qcmId;
	
	@Column(name = "sandbox_id")
	Long sandboxId;

	@PrePersist
	public void prePersist() {
		this.dateStart = new Date();
	}
}
