package fr.mipih.rh.testcandidats.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QcmService {
	
	private final QcmRepository qcmRepository;
	private final QcmMapper qcmMapper;
	private final QuestionRepository questionRepository;
	private final QuestionQcmRepository questionQcmRepository;

	
	public List<QcmDto> getAllQcm(){
		List<Qcm> qcmList = qcmRepository.findAll();
		return qcmList.stream()
				.map(qcmMapper::toDto)
				.collect(Collectors.toList());
	}

	public QcmDto saveQcm(QcmDto qcmDto) {
		QcmMapper qcmMapper = new QcmMapper();
		Qcm qcmEntity = qcmMapper.toEntity(qcmDto);
		Qcm savedQcmEntity = qcmRepository.save(qcmEntity);
		QcmDto resultDto = qcmMapper.toDto(savedQcmEntity);
		return resultDto;
	}

	@Transactional
	public void addQuestionToQcm(Long qcmId, Long questionId, Long ordre) {
		Qcm qcm = qcmRepository.findById(qcmId)
				.orElseThrow(() -> new RuntimeException("Qcm not found with id: " + qcmId));
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

		QuestionQcm questionQcm = new QuestionQcm();
		QuestionQcmId questionQcmId = new QuestionQcmId();
		questionQcmId.setIdQcm(qcm.getId());
		questionQcmId.setIdQuestion(question.getId());
		questionQcm.setQcm(qcm);
		questionQcm.setQuestion(question);
		questionQcm.setOrdre(ordre);
		questionQcm.setQuestionQcmId(questionQcmId);
		questionQcmRepository.save(questionQcm);
	}

	public QcmDto getQcm(Long id) {
		Optional<Qcm> qcmOpt = qcmRepository.findById(id);
		if(qcmOpt.isPresent()){
			Qcm qcm = qcmOpt.get();
			QcmMapper qcmMapper = new QcmMapper();
			return qcmMapper.toDto(qcm);
		}
		return null;
	}

}
