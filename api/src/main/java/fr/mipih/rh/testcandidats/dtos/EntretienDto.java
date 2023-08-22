package fr.mipih.rh.testcandidats.dtos;

import java.util.Date;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Sandbox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntretienDto {

    private Long id;
    private Admin admin;
    private Date date_end;
    private Date date_start;
    private Qcm qcm_id;
    private Sandbox sandbox_id;


}
