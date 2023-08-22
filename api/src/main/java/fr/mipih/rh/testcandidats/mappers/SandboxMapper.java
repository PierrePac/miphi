package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.models.Sandbox;

@Mapper(componentModel = "spring")
public interface SandboxMapper {

    SandboxDto toSandboxDto(Sandbox sandbox);

    Sandbox toSandboxEntity(SandboxDto sandbox);

}
