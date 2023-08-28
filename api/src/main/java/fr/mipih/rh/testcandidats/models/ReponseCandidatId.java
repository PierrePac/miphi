package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ReponseCandidatId {

    private Long idProposition;
    private Long idCandidat;
}
