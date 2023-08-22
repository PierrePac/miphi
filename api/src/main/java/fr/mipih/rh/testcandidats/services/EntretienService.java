package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Sandbox;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.EntretienMapper;
import fr.mipih.rh.testcandidats.models.Entretien;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final EntretienMapper entretienMapper;
    private final EntretienRepository entretienRepository;
    private final AdminRepository adminRepository;
    private final QcmRepository qcmRepository;
    private final SandboxRepository sandboxRepository;

    public EntretienDto findById(Long id) {
        Entretien entretien = entretienRepository.findById(id)
                .orElseThrow(() -> new AppException("Entretien introuvable", HttpStatus.NOT_FOUND));
        return entretienMapper.toEntretienDto(entretien);
    }

    public EntretienDto save(EntretienDto entretienDto) {
        Admin admin = adminRepository.findById(entretienDto.getAdmin().getId())
                .orElseThrow(() -> new AppException("Admin introuvable", HttpStatus.NOT_FOUND));
        Sandbox sandbox = sandboxRepository.findById(entretienDto.getSandbox_id().getId())
                .orElseThrow(() -> new AppException("Sandbox introuvable", HttpStatus.NOT_FOUND));
        Qcm qcm = qcmRepository.findById(entretienDto.getQcm_id().getId())
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

}
