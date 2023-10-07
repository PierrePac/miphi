package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"qcm","question"})
@Entity
@Table(name = "QUESTION_QCM")
public class QuestionQcm {

    @EmbeddedId
    private QuestionQcmId questionQcmId;

    @MapsId("id")
    @ManyToOne
    @JoinColumn(name= "idQcm")
    private Qcm qcm;

    @MapsId("id")
    @ManyToOne
    @JoinColumn(name= "idQuestion")
    private Question question;

    @Column(name = "ordre")
    private Long ordre;
}
