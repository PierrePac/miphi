package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.SandboxMapper;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SandboxService {

    private final SandboxRepository sandboxRepository;
    private final SandboxMapper sandboxMapper;
    private final CandidatRepository candidatRepository;

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

    public List<SandboxDto> getAll() {
        List<Sandbox> sandboxList = sandboxRepository.findAll();
        List<SandboxDto> sandboxDtoList = new ArrayList<>();
        for(Sandbox sandbox: sandboxList) {
            SandboxDto sandboxDto = sandboxMapper.toSandboxDto(sandbox);
            sandboxDtoList.add(sandboxDto);
        }
        return sandboxDtoList;
    }

    public void dockerComposeLauncher(CredentialsCandidatDto credentialsDto) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "docker-compose up -d");
        try{
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Docker Compose launched successfully.");
                System.out.println(output);
                System.exit(0);
            } else {
                // handle errors
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
