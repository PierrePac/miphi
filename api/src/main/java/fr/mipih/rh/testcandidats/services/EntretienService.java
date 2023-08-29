package fr.mipih.rh.testcandidats.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final EntretienMapper entretienMapper;
    private final EntretienRepository entretienRepository;
    private final AdminRepository adminRepository;
    private final QcmMapper qcmMapper;
    private final QcmRepository qcmRepository;
    private final SandboxRepository sandboxRepository;


    public EntretienDto findById(Long id) {
        Entretien entretien = entretienRepository.findById(id)
                .orElseThrow(() -> new AppException("Entretien introuvable", HttpStatus.NOT_FOUND));
        return entretienMapper.toDto(entretien);
    }

    public EntretienDto save(EntretienDto entretienDto) {
        Admin admin = adminRepository.findById(entretienDto.getAdmin().getId())
                .orElseThrow(() -> new AppException("Admin introuvable", HttpStatus.NOT_FOUND));
        Sandbox sandbox = sandboxRepository.findById(entretienDto.getSandbox().getId())
                .orElseThrow(() -> new AppException("Sandbox introuvable", HttpStatus.NOT_FOUND));
        Qcm qcm = qcmRepository.findById(entretienDto.getQcm().getId())
                .orElseThrow(() -> new AppException("Qcm introuvable", HttpStatus.NOT_FOUND));
        Entretien entretien = new Entretien();
        entretien.setDateEnd(entretienDto.getDate_end());
        entretien.setDateStart(new Date());
        //entretien.setAdmin(admin);
        entretien.setQcm(qcm);
        entretien.setSandbox(sandbox);
        entretien = entretienRepository.save(entretien);
        return entretienMapper.toDto(entretien);
    }

    public EntretienDto findDetailById(Long id) {
        Optional<Entretien> entretienOpt = entretienRepository.findById(id);
        if (entretienOpt.isPresent()) {
            Entretien entretien = entretienOpt.get();

            EntretienDto entretienDto = new EntretienDto();
            entretienDto.setId(entretien.getId());

            entretienDto.setQcm(qcmMapper.toDto(entretien.getQcm()));

        }
        return null;
    }
}
