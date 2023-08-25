package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.mipih.rh.testcandidats.dtos.AdminDto;
import fr.mipih.rh.testcandidats.dtos.NewAdminDto;
import fr.mipih.rh.testcandidats.models.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

	public AdminDto toAdminDto(Admin admin) {
		if (admin == null) {
			return null;
		}

		AdminDto adminDto = new AdminDto();
		adminDto.setId(admin.getId());
		adminDto.setNom(admin.getNom());
		adminDto.setPrenom(admin.getPrenom());
		adminDto.setRole("ADMIN"); // Assuming role is "ADMIN", you might need to adjust this
		adminDto.setToken(admin.getToken());
		adminDto.setRefreshToken(admin.getRefreshToken());

		return adminDto;
	}

	public Admin ajouterAdmin(NewAdminDto newAdminDto) {
		if (newAdminDto == null) {
			return null;
		}

		Admin admin = new Admin();
		admin.setNom(newAdminDto.getNom());
		admin.setPrenom(newAdminDto.getPrenom());
		// motDePasse should be encoded and set elsewhere, as per your original MapStruct Mapper

		return admin;
	}

	public Admin toEntity(AdminDto adminDto) {
		if (adminDto == null) {
			return null;
		}

		Admin admin = new Admin();
		admin.setId(adminDto.getId());
		admin.setNom(adminDto.getNom());
		admin.setPrenom(adminDto.getPrenom());
		admin.setToken(adminDto.getToken());
		admin.setRefreshToken(adminDto.getRefreshToken());
		// motDePasse is ignored, as per your original MapStruct Mapper

		return admin;
	}
}
