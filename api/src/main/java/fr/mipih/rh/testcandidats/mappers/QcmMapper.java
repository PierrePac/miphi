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
		qcmDto.setQuestionsQcm(toQuestionQcmDtoList(qcm.getQuestionsQcm()));
		return qcmDto;
	}

	public List<QcmDto> toDtoList(List<Qcm> qcms) {
		List<QcmDto> qcmDtos = new ArrayList<>();
		for (Qcm qcm : qcms) {
			qcmDtos.add(toDto(qcm));
		}
		return qcmDtos;
	}

	public List<QuestionQcmDto> toQuestionQcmDtoList(Set<QuestionQcm> questionQcms) {
		List<QuestionQcmDto> questionQcmDtos = new ArrayList<>();
		for (QuestionQcm questionQcm : questionQcms) {
			QuestionQcmDto questionQcmDto = new QuestionQcmDto();
			questionQcmDto.setId(questionQcm.getId());
			questionQcmDto.setOrdre(questionQcm.getOrdre());

			// Ajout des détails de la question
			if (questionQcm.getQuestion() != null) {
				QuestionDto questionDto = new QuestionDto();
				questionDto.setId(questionQcm.getQuestion().getId());  // Je suppose qu'il y a un champ id dans la Question
				// Ajoutez d'autres champs de la question ici si nécessaire
				questionQcmDto.setQuestion(questionDto);
			}

			// Ajout des détails du QCM (si nécessaire)
			if (questionQcm.getQcm() != null) {
				QcmDto qcmDto = new QcmDto();
				qcmDto.setId(questionQcm.getQcm().getId());
				// Ajoutez d'autres champs du QCM ici si nécessaire
				questionQcmDto.setQcm(qcmDto);
			}

			questionQcmDtos.add(questionQcmDto);
		}
		return questionQcmDtos;
	}

	public Qcm toEntity(QcmDto qcmDto) {
		Qcm qcm = new Qcm();
		qcm.setId(qcmDto.getId());
		qcm.setNom(qcmDto.getNom());
		qcm.setPoint(qcmDto.getPoint());
		qcm.setTemps(qcmDto.getTemps());
		qcm.setQuestionsQcm(toQuestionQcmEntitySet(qcmDto.getQuestionsQcm()));
		return qcm;
	}

	public Set<QuestionQcm> toQuestionQcmEntitySet(List<QuestionQcmDto> questionQcmDtos) {
		Set<QuestionQcm> questionQcms = new HashSet<>();
		if (questionQcmDtos != null) {
			for (QuestionQcmDto questionQcmDto : questionQcmDtos) {
				QuestionQcm questionQcm = new QuestionQcm();
				questionQcm.setId(questionQcmDto.getId());
				questionQcms.add(questionQcm);
			}
		}
		return questionQcms;
	}
}
