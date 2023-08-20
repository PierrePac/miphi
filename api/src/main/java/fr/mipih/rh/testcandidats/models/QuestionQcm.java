package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QUESTION_QCM")
public class QuestionQcm {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "qcm_id")
    private Qcm qcm;

    @Column(name = "ordre")
    private int ordre;
}
