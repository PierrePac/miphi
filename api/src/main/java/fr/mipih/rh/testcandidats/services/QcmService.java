package fr.mipih.rh.testcandidats.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;

@Service
public class QcmService {
	
	private final QcmRepository qcmRepository;
	private final QcmMapper qcmMapper;
	private final QuestionMapper questionMapper;
	private final QuestionRepository questionRepository;
	
	@Autowired
	public QcmService(QcmRepository qcmRepository, QcmMapper qcmMapper, QuestionMapper questionMapper, QuestionRepository questionRepository) {
		this.qcmRepository = qcmRepository;
		this.qcmMapper = qcmMapper;
		this.questionMapper = questionMapper;
		this.questionRepository = questionRepository;
	}
	
	public List<QcmDto> getAllQcm(){
		List<Qcm> qcmList = qcmRepository.findAll();
		return qcmList.stream()
				.map(qcmMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public QcmDto saveQcm(QcmDto qcmDto) {
		//return qcmMapper.toDto(qcmRepository.save(qcmMapper.toEntity(qcmDto)));
		System.out.println("Avant conversion en entité: " + qcmDto.getNom());

		Qcm qcmEntity = qcmMapper.toEntity(qcmDto);
		System.out.println("Après conversion en entité: " + qcmEntity.getNom());

		Qcm savedQcmEntity = qcmRepository.save(qcmEntity);
		System.out.println("Après sauvegarde dans la BD: " + savedQcmEntity.getNom());

		QcmDto resultDto = qcmMapper.toDto(savedQcmEntity);
		System.out.println("Après conversion en DTO: " + resultDto.getNom());

		return resultDto;
	}

	@Transactional
	public void addQuestionToQcm(Long qcmId, Long questionId) {
		Qcm qcm = qcmRepository.findById(qcmId)
				.orElseThrow(() -> new RuntimeException("Qcm not found with id: " + qcmId));
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

		qcm.getQuestions().add(question);
		question.getQcms().add(qcm);
		qcmRepository.save(qcm);
	}
	
	public List<QcmDto> getAllQcms() {
		List<Qcm> qcmList = qcmRepository.findAll();
		return qcmList.stream()
				.map(qcm -> {
					QcmDto qcmDto = qcmMapper.toDto(qcm);
					Set<Question> questions = qcm.getQuestions();
					qcmDto.setQuestions(questions.stream().map(q -> {
						QuestionDto questionDto = new QuestionDto();
						questionDto.setId(q.getId());
						return questionDto;
					}).collect(Collectors.toList()));
					return qcmDto;
				}).collect(Collectors.toList());
	}

}
