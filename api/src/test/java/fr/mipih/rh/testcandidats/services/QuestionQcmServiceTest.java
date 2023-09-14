package fr.mipih.rh.testcandidats.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionQcmServiceTest {

    private QuestionQcmService questionQcmService;
    private final QuestionQcmRepository questionQcmRepository = mock(QuestionQcmRepository.class);
    private final QuestionQcmMapper questionQcmMapper = mock(QuestionQcmMapper.class);

    @BeforeEach
    public void setUp() {
        questionQcmService = new QuestionQcmService(questionQcmRepository, questionQcmMapper);
    }

    @Test
    public void testUpdateOrder() {
        QuestionQcmDto dto = new QuestionQcmDto();
        dto.setIdQcm(1L);
        dto.setIdQuestion(2L);
        dto.setOrdre(3L);

        QuestionQcm entity = new QuestionQcm();
        QuestionQcmId id = new QuestionQcmId(1L, 2L);

        when(questionQcmRepository.findById(id)).thenReturn(Optional.of(entity));
        when(questionQcmMapper.toDto(entity)).thenReturn(dto);

        QuestionQcmDto updatedDto = questionQcmService.updateOrder(dto);

        assertEquals(dto, updatedDto);
        verify(questionQcmRepository, times(1)).save(entity);
    }

    @Test
    public void testUpdateOrder_EntityNotFoundException() {
        QuestionQcmDto dto = new QuestionQcmDto();
        dto.setIdQcm(1L);
        dto.setIdQuestion(2L);
        QuestionQcmId id = new QuestionQcmId(1L, 2L);

        when(questionQcmRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            questionQcmService.updateOrder(dto);
        });
    }

    @Test
    public void testGetQuestionQcm() {
        QuestionQcm entity = new QuestionQcm();
        QuestionQcmDto dto = new QuestionQcmDto();

        when(questionQcmRepository.findAllByQuestionQcmIdIdQcm(1L)).thenReturn(Arrays.asList(entity));
        when(questionQcmMapper.toDto(entity)).thenReturn(dto);

        List<QuestionQcmDto> returnedDtos = questionQcmService.getQuestionQcm(1L);

        assertNotNull(returnedDtos);
        assertEquals(1, returnedDtos.size());
        assertEquals(dto, returnedDtos.get(0));
    }
}

