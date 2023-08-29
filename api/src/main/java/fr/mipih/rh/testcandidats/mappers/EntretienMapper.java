package fr.mipih.rh.testcandidats.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.models.Entretien;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntretienMapper {


    private final AdminMapper adminMapper;
    private final QcmMapper qcmMapper;
    private final SandboxMapper sandboxMapper;

    public EntretienDto toDto(Entretien entretien) {
        if (entretien == null) {
            return null;
        }

        EntretienDto entretienDto = new EntretienDto();

        entretienDto.setId(entretien.getId());


        //entretienDto.setAdmin(adminMapper.toAdminDto(entretien.getAdmin()));
        entretienDto.setQcm(qcmMapper.toDto(entretien.getQcm()));
        entretienDto.setSandbox(sandboxMapper.toSandboxDto(entretien.getSandbox()));

        return entretienDto;
    }

    public Entretien toEntity(EntretienDto entretienDto) {
        if (entretienDto == null) {
            return null;
        }

        Entretien entretien = new Entretien();

        entretien.setId(entretienDto.getId());
        // ... (autres champs)

        //entretien.setAdmin(adminMapper.toEntity(entretienDto.getAdmin()));
        entretien.setQcm(qcmMapper.toEntity(entretienDto.getQcm()));
        entretien.setSandbox(sandboxMapper.toSandboxEntity(entretienDto.getSandbox()));

        return entretien;
    }

}
