package fr.mipih.rh.testcandidats.mappers;

import org.mapstruct.Mapper;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.models.Qcm;

@Mapper(componentModel = "spring")
public interface QcmMapper {
	
	QcmDto toDto(Qcm qcm);
	
	default Qcm toEntity(QcmDto qcmDto) {
		Qcm qcm = new Qcm();
		qcm.setNom(qcmDto.getNom());
		qcm.setTemps(qcmDto.getTemps());
		qcm.setPoint(qcmDto.getPoint());
		return qcm;
	}

}
