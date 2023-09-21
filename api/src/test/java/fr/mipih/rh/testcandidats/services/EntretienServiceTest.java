package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.SandboxDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.EntretienMapper;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class EntretienServiceTest {
    @InjectMocks
    EntretienService entretienService;

    @Mock
    EntretienRepository entretienRepository;
    @Mock
    AdminRepository adminRepository;
    @Mock
    QcmRepository qcmRepository;
    @Mock
    SandboxRepository sandboxRepository;
    @Mock
    EntretienMapper entretienMapper;
    @Mock
    QcmMapper qcmMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindById() {
        Long id = 1L;
        Entretien entretien = new Entretien();
        when(entretienRepository.findById(id)).thenReturn(Optional.of(entretien));
        when(entretienMapper.toDto(entretien)).thenReturn(new EntretienDto());

        assertNotNull(entretienService.findById(id));
        verify(entretienRepository).findById(id);
        verify(entretienMapper).toDto(entretien);
    }

    @Test
    void shouldThrowExceptionWhenFindById() {
        Long id = 1L;
        when(entretienRepository.findById(id)).thenReturn(Optional.empty());

        AppException thrown = assertThrows(
                AppException.class,
                () -> entretienService.findById(id)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
        assertEquals("Entretien introuvable", thrown.getMessage());
    }

    @Test
    public void shouldFindDetailById() {
        // Arrange
        Long entretienId = 1L;
        Entretien mockEntretien = mock(Entretien.class);
        Qcm mockQcm = mock(Qcm.class);
        when(mockEntretien.getQcm()).thenReturn(mockQcm); // Assurez-vous que getQcm() renvoie un objet mocké non-null.
        when(entretienRepository.findById(entretienId)).thenReturn(Optional.of(mockEntretien));
        QcmDto mockQcmDto = mock(QcmDto.class);
        when(qcmMapper.toDto(mockQcm)).thenReturn(mockQcmDto); // Changez any(Qcm.class) par mockQcm pour plus de précision.

        // Act
        EntretienDto result = entretienService.findDetailById(entretienId);

        // Assert
        assertNotNull(result);
        assertEquals(mockQcmDto, result.getQcm());
    }

    @Test
    public void shouldThrowNoSuchElementExceptionWhenEntretienNotFound() {
        // Arrange
        Long entretienId = 1L;
        when(entretienRepository.findById(entretienId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> entretienService.findDetailById(entretienId));
    }

    @Test
    void shouldGetAll() {
        Entretien entretien = new Entretien();
        when(entretienRepository.findAll()).thenReturn(Collections.singletonList(entretien));
        when(entretienMapper.toDto(entretien)).thenReturn(new EntretienDto());

        assertFalse(entretienService.getAll().isEmpty());
        verify(entretienRepository).findAll();
        verify(entretienMapper).toDto(entretien);
    }
}
