package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.CandidatDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsCandidatDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.CandidatMapper;
import fr.mipih.rh.testcandidats.models.Candidat;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CandidatServiceTest {
    @InjectMocks
    private CandidatService candidatService;

    @Mock
    private CandidatMapper candidatMapper;

    @Mock
    private CandidatRepository candidatRepository;

    @Mock
    private EntretienRepository entretienRepository;

    @Test
    void testLoginCandidatSuccess() {
        CredentialsCandidatDto credentialsDto = new CredentialsCandidatDto("nom", "prenom");
        Candidat candidat = new Candidat();
        candidat.setPrenom("prenom");
        candidat.setEntretien(new Entretien());

        Mockito.when(candidatRepository.findByNom("nom")).thenReturn(Optional.of(candidat));
        Mockito.when(candidatMapper.toCandidatDto(candidat)).thenReturn(new CandidatDto());

        CandidatDto result = candidatService.loginCandidat(credentialsDto);

        assertNotNull(result);
    }

    @Test
    void testLoginCandidatFail() {
        CredentialsCandidatDto credentialsDto = new CredentialsCandidatDto("nom", "prenom");

        Mockito.when(candidatRepository.findByNom("nom")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () -> {
            candidatService.loginCandidat(credentialsDto);
        });

        assertEquals("Candidat Inconnu", exception.getMessage());
    }

    @Test
    void testFindByNomSuccess() {
        Candidat candidat = new Candidat();

        Mockito.when(candidatRepository.findByNom("nom")).thenReturn(Optional.of(candidat));
        Mockito.when(candidatMapper.toCandidatDto(candidat)).thenReturn(new CandidatDto());

        CandidatDto result = candidatService.findByNom("nom");

        assertNotNull(result);
    }

    @Test
    void testFindByNomFail() {
        Mockito.when(candidatRepository.findByNom("nom")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () -> {
            candidatService.findByNom("nom");
        });

        assertEquals("Candidat Inconnu", exception.getMessage());
    }
    @Test
    void testSave() {
        CandidatDto candidatDto = new CandidatDto();
        candidatDto.setEntretienId(1L);

        Candidat candidat = new Candidat();
        Entretien entretien = new Entretien();

        Mockito.when(entretienRepository.findById(1L)).thenReturn(Optional.of(entretien));
        Mockito.when(candidatRepository.save(candidat)).thenReturn(candidat);
        Mockito.when(candidatMapper.toEntity(candidatDto)).thenReturn(candidat);
        Mockito.when(candidatMapper.toCandidatDto(candidat)).thenReturn(candidatDto);

        CandidatDto result = candidatService.save(candidatDto);

        assertNotNull(result);
        assertEquals(candidatDto, result);
    }
}
