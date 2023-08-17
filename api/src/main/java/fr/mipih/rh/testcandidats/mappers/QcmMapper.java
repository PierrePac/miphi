package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.models.Qcm;

@Mapper(componentModel = "spring")
public interface QcmMapper {
	
	QcmDto toDto(Qcm qcm);
	
	Qcm toEntity(QcmDto qcmDto);

}
