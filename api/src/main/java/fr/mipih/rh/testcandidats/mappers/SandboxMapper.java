package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.models.enums.Niveau;
import fr.mipih.rh.testcandidats.models.enums.Technologie;
import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.models.Sandbox;
import org.springframework.stereotype.Component;

@Component
public class SandboxMapper {

    public SandboxDto toSandboxDto(Sandbox sandbox) {
        if (sandbox == null) {
            return null;
        }

        SandboxDto sandboxDto = new SandboxDto();
        sandboxDto.setId(sandbox.getId());
        sandboxDto.setSrc(sandbox.getSrc());
        sandboxDto.setNiveau(sandbox.getNiveau().name());
        sandboxDto.setTechnologie(sandbox.getTechnologie().name());
        sandboxDto.setNom(sandbox.getNom());
        sandboxDto.setConsigne(sandbox.getConsigne());

        return sandboxDto;
    }

    public Sandbox toSandboxEntity(SandboxDto sandboxDto) {
        if (sandboxDto == null) {
            return null;
        }

        Sandbox sandbox = new Sandbox();
        sandbox.setId(sandboxDto.getId());
        sandbox.setSrc(sandboxDto.getSrc());
        sandbox.setNiveau(Niveau.valueOf(sandboxDto.getNiveau()));
        sandbox.setTechnologie(Technologie.valueOf(sandboxDto.getTechnologie()));
        sandbox.setNom(sandboxDto.getNom());
        sandbox.setConsigne(sandboxDto.getConsigne());

        return sandbox;
    }
}
