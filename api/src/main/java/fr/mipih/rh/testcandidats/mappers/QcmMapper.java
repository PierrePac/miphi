package fr.mipih.rh.testcandidats.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import org.springframework.stereotype.Component;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.QuestionQcm;

@Component
public class QcmMapper {

	public QcmDto toDto(Qcm qcm) {
		QcmDto qcmDto = new QcmDto();
		qcmDto.setId(qcm.getId());
		qcmDto.setNom(qcm.getNom());
		qcmDto.setPoint(qcm.getPoint());
		qcmDto.setTemps(qcm.getTemps());
		return qcmDto;
	}

	public List<QcmDto> toDtoList(List<Qcm> qcms) {
		List<QcmDto> qcmDtos = new ArrayList<>();
		for (Qcm qcm : qcms) {
			qcmDtos.add(toDto(qcm));
		}
		return qcmDtos;
	}

	public Qcm toEntity(QcmDto qcmDto) {
		Qcm qcm = new Qcm();
		qcm.setId(qcmDto.getId());
		qcm.setNom(qcmDto.getNom());
		qcm.setPoint(qcmDto.getPoint());
		qcm.setTemps(qcmDto.getTemps());
		return qcm;
	}
}
