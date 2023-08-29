package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.ReponseCandidatDto;
import fr.mipih.rh.testcandidats.mappers.ReponseCandidatMapper;
import fr.mipih.rh.testcandidats.models.Proposition;
import fr.mipih.rh.testcandidats.models.ReponseCandidat;
import fr.mipih.rh.testcandidats.repositories.CandidatRepository;
import fr.mipih.rh.testcandidats.repositories.PropositionRepository;
import fr.mipih.rh.testcandidats.repositories.ReponseCandidatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReponseCandidatService {

    private final ReponseCandidatMapper reponseCandidatMapper;
    private final ReponseCandidatRepository reponseCandidatRepository;
    private final PropositionRepository propositionRepository;
    private final CandidatRepository candidatRepository;

    private List<ReponseCandidat> repositoryEntity = new ArrayList<>();

    public List<ReponseCandidat> saveAll(List<ReponseCandidatDto> reponseCandidatDtoList) {
        List<ReponseCandidat> entities = new ArrayList<>();
        for (ReponseCandidatDto reponseCandidatDto: reponseCandidatDtoList) {
            ReponseCandidat reponseCandidat = reponseCandidatMapper.toEntity(reponseCandidatDto, propositionRepository, candidatRepository);
            reponseCandidatRepository.save(reponseCandidat);
            repositoryEntity.add(reponseCandidat);
            entities.add(reponseCandidat);
        }
        return entities;
    }

    public List<ReponseCandidatDto> getResponseCandidatByCandidat(Long id) {
        List<ReponseCandidatDto> reponseDto = new ArrayList<>();
        List<ReponseCandidat> reponseCandidatList = reponseCandidatRepository.findAllByReponseCandidatIdIdCandidat(id);
        for(ReponseCandidat reponseCandidat: reponseCandidatList) {
            ReponseCandidatDto reponseCandidatDto = reponseCandidatMapper.toDto(reponseCandidat);
            reponseDto.add(reponseCandidatDto);
        }
    return reponseDto;
    }


}
