package fr.mipih.rh.testcandidats.models;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionQcmId {
    
    private Long idQcm;
    private Long idQuestion;
}
