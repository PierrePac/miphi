package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"proposition","candidat"})
@EqualsAndHashCode(exclude = {"proposition", "candidat"})
@Entity
public class ReponseCandidat {

    @EmbeddedId
    private ReponseCandidatId reponseCandidatId;

    @MapsId("idProposition")
    @ManyToOne
    @JoinColumn(name = "id_proposition")
    private Proposition proposition;

    @MapsId("idCandidat")
    @ManyToOne
    @JoinColumn(name = "id_candidat")
    private Candidat candidat;

}
