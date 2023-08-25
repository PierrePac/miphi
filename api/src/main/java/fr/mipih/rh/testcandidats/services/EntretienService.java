package fr.mipih.rh.testcandidats.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.AdminMapper;
import fr.mipih.rh.testcandidats.mappers.EntretienMapper;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.mappers.SandboxMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final EntretienMapper entretienMapper;
    private final EntretienRepository entretienRepository;
    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;
    private final QcmMapper qcmMapper;
    private final QcmRepository qcmRepository;
    private final QuestionQcmRepository questionQcmRepository;
    private final SandboxMapper sandboxMapper;
    private final SandboxRepository sandboxRepository;
    private final QuestionQcmMapper questionQcmMapper;

    public EntretienDto findById(Long id) {
        Entretien entretien = entretienRepository.findById(id)
                .orElseThrow(() -> new AppException("Entretien introuvable", HttpStatus.NOT_FOUND));
        return entretienMapper.toEntretienDto(entretien);
    }

    public EntretienDto save(EntretienDto entretienDto) {
        Admin admin = adminRepository.findById(entretienDto.getAdmin().getId())
                .orElseThrow(() -> new AppException("Admin introuvable", HttpStatus.NOT_FOUND));
        Sandbox sandbox = sandboxRepository.findById(entretienDto.getSandbox().getId())
                .orElseThrow(() -> new AppException("Sandbox introuvable", HttpStatus.NOT_FOUND));
        Qcm qcm = qcmRepository.findById(entretienDto.getQcm().getId())
                .orElseThrow(() -> new AppException("Qcm introuvable", HttpStatus.NOT_FOUND));
        Entretien entretien = entretienMapper.toEntretienEntity(entretienDto);
        entretien.setDateEnd(entretienDto.getDate_end());
        entretien.setDateStart(new Date());
        entretien.setAdmin(admin);
        entretien.setQcm(qcm);
        entretien.setSandbox(sandbox);
        entretien = entretienRepository.save(entretien);
        return entretienMapper.toEntretienDto(entretien);
    }

    public EntretienDto findDetailById(Long id) {
        Optional<Entretien> entretienOpt = entretienRepository.findById(id);
        if (entretienOpt.isPresent()) {
            Entretien entretien = entretienOpt.get();
            Hibernate.initialize(entretien.getQcm());

            EntretienDto entretienDto = new EntretienDto();
            entretienDto.setId(entretien.getId());
            entretienDto.setDate_end(entretien.getDateEnd());
            entretienDto.setDate_start(entretien.getDateStart());

            entretienDto.setAdmin(adminMapper.toAdminDto(entretien.getAdmin()));
            entretienDto.setQcm(qcmMapper.toDto(entretien.getQcm()));
            // rechercher questionQcm
           
            Optional<List<QuestionQcm>> questionQcmListOpt = questionQcmRepository.findAllByQcmId(entretienDto.getQcm().getId());
            if(questionQcmListOpt.isPresent()) {
            	List<QuestionQcm> questionQcmList = questionQcmListOpt.get();
                List<QuestionQcmDto> questionQcmDtoList = new ArrayList<>();
                
                for (QuestionQcm questionQcm : questionQcmList) {
                    questionQcmDtoList.add(questionQcmMapper.toDto(questionQcm));
                }
                
                entretienDto.setQuestionQcms(questionQcmDtoList);
            }
            //entretienDto.setQuestionQcms(questionQcmDto);
            entretienDto.setSandbox(sandboxMapper.toSandboxDto(entretien.getSandbox()));

            return entretienDto;
        }
        return null;
    }
}
