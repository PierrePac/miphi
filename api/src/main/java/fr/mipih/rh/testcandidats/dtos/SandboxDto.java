package fr.mipih.rh.testcandidats.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SandboxDto {

    private Long id;
    private String src;
    private String categorie;
    private String niveau;
    private String technologie;

}
