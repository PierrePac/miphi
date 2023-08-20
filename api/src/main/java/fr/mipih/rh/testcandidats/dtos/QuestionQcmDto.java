package fr.mipih.rh.testcandidats.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionQcmDto {

    private Long qcmId;
    private List<Long> questionIds;
    private int ordre;
}
