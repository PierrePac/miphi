package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.CredentialsAdminDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;
import fr.mipih.rh.testcandidats.mappers.AdminMapper;
import fr.mipih.rh.testcandidats.models.Admin;
import fr.mipih.rh.testcandidats.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @InjectMocks
    AdminService adminService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AdminMapper adminMapper;

    @Mock
    AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginAdmin() {
        // Arrange
        CredentialsAdminDto credentialsDto = new CredentialsAdminDto("nom", "motDePasse".toCharArray());
        Admin admin = new Admin();
        AdminDto adminDto = new AdminDto();

        when(adminRepository.findByNom("nom")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(CharBuffer.wrap("motDePasse"), admin.getMotDePasse())).thenReturn(true);
        when(adminMapper.toAdminDto(admin)).thenReturn(adminDto);

        // Act
        AdminDto result = adminService.loginAdmin(credentialsDto);

        // Assert
        assertEquals(adminDto, result);
    }

    @Test
    void testLoginAdminAdminInconnu() {
        // Arrange
        CredentialsAdminDto credentialsDto = new CredentialsAdminDto("nom", "motDePasse".toCharArray());

        when(adminRepository.findByNom("nom")).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(AppException.class, () -> adminService.loginAdmin(credentialsDto));
    }

    @Test
    void testAjoutAdmin() {
        // Arrange
        NewAdminDto newAdminDto = new NewAdminDto("nom", "prenom", "motDePasse".toCharArray());
        Admin admin = new Admin();
        AdminDto adminDto = new AdminDto();

        when(adminRepository.findByNom("nom")).thenReturn(Optional.empty());
        when(adminMapper.ajouterAdmin(newAdminDto)).thenReturn(admin);
        when(passwordEncoder.encode(CharBuffer.wrap(newAdminDto.getMotDePasse()))).thenReturn("EncodedPassword");
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminMapper.toAdminDto(admin)).thenReturn(adminDto);

        // Act
        AdminDto result = adminService.ajoutAdmin(newAdminDto);

        // Assert
        assertEquals(adminDto, result);
    }

    @Test
    void testAjoutAdminDoublon() {
        // Arrange
        NewAdminDto newAdminDto = new NewAdminDto("nom", "prenom", "motDePasse".toCharArray());
        Admin admin = new Admin();

        when(adminRepository.findByNom("nom")).thenReturn(Optional.of(admin));

        // Act / Assert
        assertThrows(AppException.class, () -> adminService.ajoutAdmin(newAdminDto));
    }
}
