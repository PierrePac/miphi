package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.SandboxMapper;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SandboxServiceTest {

    private final SandboxRepository sandboxRepository = mock(SandboxRepository.class);
    private final SandboxMapper sandboxMapper = mock(SandboxMapper.class);
    private final SandboxService sandboxService = new SandboxService(sandboxRepository, sandboxMapper);

    @Test
    public void testFindById(){
        Sandbox sandbox = new Sandbox();
        SandboxDto sandboxDto = new SandboxDto();

        when(sandboxRepository.findById(1L)).thenReturn(Optional.of(sandbox));
        when(sandboxMapper.toSandboxDto(sandbox)).thenReturn(sandboxDto);

        SandboxDto result = sandboxService.findById(1L);

        assertNotNull(result);
        assertEquals(sandboxDto, result);
    }

    @Test
    public void testFindByIdThrowsException() {
        when(sandboxRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            sandboxService.findById(1L);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals("Sandbox not found", e.getMessage());
        }
    }

    @Test
    public void testSave() {
        Sandbox sandbox = new Sandbox();
        SandboxDto sandboxDto = new SandboxDto();

        when(sandboxMapper.toSandboxEntity(sandboxDto)).thenReturn(sandbox);
        when(sandboxRepository.save(sandbox)).thenReturn(sandbox);
        when(sandboxMapper.toSandboxDto(sandbox)).thenReturn(sandboxDto);

        SandboxDto result = sandboxService.save(sandboxDto);

        assertNotNull(result);
        assertEquals(sandboxDto, result);
    }

    @Test
    public void testGetAll() {
        Sandbox sandbox1 = new Sandbox();
        Sandbox sandbox2 = new Sandbox();
        SandboxDto sandboxDto1 = new SandboxDto();
        SandboxDto sandboxDto2 = new SandboxDto();

        when(sandboxRepository.findAll()).thenReturn(Arrays.asList(sandbox1, sandbox2));
        when(sandboxMapper.toSandboxDto(sandbox1)).thenReturn(sandboxDto1);
        when(sandboxMapper.toSandboxDto(sandbox2)).thenReturn(sandboxDto2);

        List<SandboxDto> result = sandboxService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(sandboxDto1, result.get(0));
        assertEquals(sandboxDto2, result.get(1));
    }
}
