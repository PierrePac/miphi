package fr.mipih.rh.testcandidats.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.SandboxMapper;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SandboxService {

    private final SandboxRepository sandboxRepository;
    private final SandboxMapper sandboxMapper;

    public SandboxDto findById(Long id) {
        Sandbox sandbox = sandboxRepository.findById(id)
                .orElseThrow(() -> new AppException("Sandbox not found", HttpStatus.NOT_FOUND));
        return sandboxMapper.toSandboxDto(sandbox);
    }

    public SandboxDto save(SandboxDto sandboxDto) {
        Sandbox sandbox = sandboxMapper.toSandboxEntity(sandboxDto);
        sandbox = sandboxRepository.save(sandbox);
        return sandboxMapper.toSandboxDto(sandbox);
    }
}
