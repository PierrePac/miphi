package fr.mipih.rh.testcandidats.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.mappers.ReponseCandidatMapper;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import fr.mipih.rh.testcandidats.repositories.ReponseCandidatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReponseCandidatServiceTest {

    @InjectMocks
    private ReponseCandidatService reponseCandidatService;

    @Mock
    private ReponseCandidatMapper reponseCandidatMapper;

    @Mock
    private ReponseCandidatRepository reponseCandidatRepository;

    @Mock
    private PropositionRepository propositionRepository;

    @Mock
    private CandidatRepository candidatRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSaveAll() {
        try (MockedStatic<ReponseCandidatMapper> mocked = mockStatic(ReponseCandidatMapper.class)) {
            ReponseCandidatDto dto1 = new ReponseCandidatDto();
            ReponseCandidat entity1 = new ReponseCandidat();

            mocked.when(() -> ReponseCandidatMapper.toEntity(eq(dto1), any(), any())).thenReturn(entity1);

            List<ReponseCandidatDto> dtoList = Arrays.asList(dto1);
            List<ReponseCandidat> savedEntities = reponseCandidatService.saveAll(dtoList);

            assertNotNull(savedEntities);
            assertEquals(1, savedEntities.size());
            assertEquals(entity1, savedEntities.get(0));

            mocked.reset();

            mocked.when(() -> ReponseCandidatMapper.toEntity(eq(dto1), any(), any())).thenReturn(null);

            savedEntities = reponseCandidatService.saveAll(dtoList);

            assertNotNull(savedEntities);
        }
    }


    @Test
    public void testGetResponseCandidatByCandidat() {
        try (MockedStatic<ReponseCandidatMapper> mocked = mockStatic(ReponseCandidatMapper.class)) {
            List<ReponseCandidat> existingEntities = new ArrayList<>();
            ReponseCandidat existingEntity1 = new ReponseCandidat();
            existingEntities.add(existingEntity1);

            ReponseCandidatDto dto1 = new ReponseCandidatDto();

            when(reponseCandidatRepository.findAllByReponseCandidatIdIdCandidat(1L)).thenReturn(existingEntities);

            mocked.when(() -> ReponseCandidatMapper.toDto(existingEntity1)).thenReturn(dto1);

            List<ReponseCandidatDto> returnedDtos = reponseCandidatService.getResponseCandidatByCandidat(1L);

            assertNotNull(returnedDtos);
            assertEquals(1, returnedDtos.size());
            assertEquals(dto1, returnedDtos.get(0));
        }
    }
}
