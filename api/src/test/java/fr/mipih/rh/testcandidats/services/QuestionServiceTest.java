package fr.mipih.rh.testcandidats.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class QuestionServiceTest {

    private QuestionService questionService;
    private QuestionRepository questionRepository = mock(QuestionRepository.class);
    private QuestionQcmRepository questionQcmRepository = mock(QuestionQcmRepository.class);

    @BeforeEach
    public void setUp() {
        questionService = new QuestionService(questionRepository, questionQcmRepository);
    }

    @Test
    public void testSaveQuestion() {
        try (MockedStatic<QuestionMapper> mocked = Mockito.mockStatic(QuestionMapper.class)) {
            QuestionDto dto = new QuestionDto();
            Question entity = new Question();

            mocked.when(() -> QuestionMapper.toEntity(dto)).thenReturn(entity);
            mocked.when(() -> QuestionMapper.toDto(entity)).thenReturn(dto);

            when(questionRepository.save(entity)).thenReturn(entity);

            QuestionDto savedDto = questionService.saveQuestion(dto);

            assertNotNull(savedDto);
            assertEquals(dto, savedDto);
        }
    }

    @Test
    public void testGetAllQuestions() {
        try (MockedStatic<QuestionMapper> mocked = Mockito.mockStatic(QuestionMapper.class)) {
            Question entity = new Question();
            QuestionDto dto = new QuestionDto();

            mocked.when(() -> QuestionMapper.toGetAllDto(entity)).thenReturn(dto);

            when(questionRepository.findAll()).thenReturn(Arrays.asList(entity));

            List<QuestionDto> dtos = questionService.getAllQuestions();

            assertNotNull(dtos);
            assertEquals(1, dtos.size());
            assertEquals(dto, dtos.get(0));
        }
    }

    @Test
    public void testDeleteQuestion() {
        Long id = 1L;
        doNothing().when(questionRepository).deleteById(id);

        questionService.deleteQuestion(id);

        verify(questionRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetAllQuestionOfQcm() {
        try (MockedStatic<QuestionMapper> mocked = Mockito.mockStatic(QuestionMapper.class)) {
            Question entity = new Question();
            QuestionDto dto = new QuestionDto();
            QuestionQcm questionQcm = new QuestionQcm();
            questionQcm.setQuestion(entity);
            Long id = 1L;

            mocked.when(() -> QuestionMapper.toDto(entity)).thenReturn(dto);

            when(questionQcmRepository.findAllByQuestionQcmIdIdQcm(id)).thenReturn(Arrays.asList(questionQcm));
            when(questionRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

            List<QuestionDto> dtos = questionService.getAllQuestionOfQcm(id);

            assertNotNull(dtos);
            assertEquals(1, dtos.size());
            assertEquals(dto, dtos.get(0));
        }
    }
}
