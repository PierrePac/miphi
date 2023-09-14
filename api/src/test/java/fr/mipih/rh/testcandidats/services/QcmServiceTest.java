package fr.mipih.rh.testcandidats.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class QcmServiceTest {

    private QcmService qcmService;
    private final QcmRepository qcmRepository = mock(QcmRepository.class);
    private final QuestionRepository questionRepository = mock(QuestionRepository.class);
    private final QuestionQcmRepository questionQcmRepository = mock(QuestionQcmRepository.class);
    private final QcmMapper qcmMapper = mock(QcmMapper.class);

    @BeforeEach
    public void setUp() {
        qcmService = new QcmService(qcmRepository, qcmMapper, questionRepository, questionQcmRepository);
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetAllQcm() {
        Qcm qcm = new Qcm();
        QcmDto qcmDto = new QcmDto();

        when(qcmRepository.findAll()).thenReturn(Arrays.asList(qcm));
        when(qcmMapper.toDto(qcm)).thenReturn(qcmDto);

        List<QcmDto> returnedDtos = qcmService.getAllQcm();

        assertNotNull(returnedDtos);
        assertEquals(1, returnedDtos.size());
        assertEquals(qcmDto, returnedDtos.get(0));
    }

//    @Test
//    public void testSaveQcm() {
//        QcmDto dto = new QcmDto();
//        Qcm entity = new Qcm();
//
//        when(qcmMapper.toEntity(dto)).thenReturn(entity);
//        when(qcmRepository.save(entity)).thenReturn(entity);
//        when(qcmMapper.toDto(entity)).thenReturn(dto);
//
//        QcmDto savedDto = qcmService.saveQcm(dto);
//
//        assertEquals(dto, savedDto);
//    }

    @Test
    public void testAddQuestionToQcm() {
        Long qcmId = 1L;
        Long questionId = 2L;
        Long ordre = 3L;

        Qcm qcm = new Qcm();
        Question question = new Question();

        when(qcmRepository.findById(qcmId)).thenReturn(Optional.of(qcm));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        qcmService.addQuestionToQcm(qcmId, questionId, ordre);

        verify(questionQcmRepository, times(1)).save(any(QuestionQcm.class));
    }

    @Test
    public void testGetQcm() {
        Long id = 1L;
        Qcm qcm = new Qcm();
        QcmDto dto = new QcmDto();

        when(qcmRepository.findById(id)).thenReturn(Optional.of(qcm));
        when(qcmMapper.toDto(qcm)).thenReturn(dto);

        QcmDto returnedDto = qcmService.getQcm(id);

        assertEquals(dto, returnedDto);
    }

    @Test
    public void testGetQcm_NotFound() {
        Long id = 1L;

        when(qcmRepository.findById(id)).thenReturn(Optional.empty());

        QcmDto returnedDto = qcmService.getQcm(id);

        assertNull(returnedDto);
    }
}
