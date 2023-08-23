package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.*;
import fr.mipih.rh.testcandidats.models.*;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.SandboxRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.mipih.rh.testcandidats.dtos.EntretienDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.repositories.EntretienRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final EntretienMapper entretienMapper;
    private final EntretienRepository entretienRepository;
    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;
    private final QcmMapper qcmMapper;
    private final QcmRepository qcmRepository;
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

            // Création d'un nouvel objet EntretienDto
            EntretienDto entretienDto = new EntretienDto();

            // Remplissage de EntretienDto avec les données de l'objet Entretien
            entretienDto.setId(entretien.getId());
            entretienDto.setDate_end(entretien.getDateEnd());
            entretienDto.setDate_start(entretien.getDateStart());

            // Conversion des objets Admin, Qcm et Sandbox en leurs DTO respectifs
            // J'utilise des "mappers" hypothétiques ici pour cette conversion,
            // vous devrez implémenter ces méthodes vous-même ou utiliser une bibliothèque de mapping.
            entretienDto.setAdmin(adminMapper.toAdminDto(entretien.getAdmin()));
            entretienDto.setQcm(qcmMapper.toDto(entretien.getQcm()));
            entretienDto.setSandbox(sandboxMapper.toSandboxDto(entretien.getSandbox()));

            return entretienDto;
        }
        return null;
    }
}
