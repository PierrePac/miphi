package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.models.Proposition;
import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PropositionServiceTest {

    @InjectMocks
    PropositionService propositionService;

    @Mock
    PropositionRepository propositionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddReponse() {
        PropositionDto propositionDto = new PropositionDto();
        propositionDto.setId(1L);

        Proposition proposition = new Proposition();
        proposition.setId(1L);

        when(propositionRepository.save(any(Proposition.class))).thenReturn(proposition);

        PropositionDto result = propositionService.addReponse(propositionDto);

        assertEquals(1L, result.getId());
        verify(propositionRepository).save(any(Proposition.class));
    }

    @Test
    void shouldGetAllReponses() {
        Proposition proposition1 = new Proposition();
        proposition1.setId(1L);

        Proposition proposition2 = new Proposition();
        proposition2.setId(2L);

        when(propositionRepository.findAll()).thenReturn(Arrays.asList(proposition1, proposition2));

        List<PropositionDto> results = propositionService.getAllReponses();

        assertEquals(2, results.size());
        verify(propositionRepository).findAll();
    }

    @Test
    void shouldDeleteReponse() {
        Long id = 1L;

        doNothing().when(propositionRepository).deleteById(id);

        propositionService.deleteReponse(id);

        verify(propositionRepository).deleteById(id);
    }
}
