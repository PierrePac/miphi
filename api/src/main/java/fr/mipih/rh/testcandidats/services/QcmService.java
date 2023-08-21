package fr.mipih.rh.testcandidats.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.QcmMapper;
import fr.mipih.rh.testcandidats.mappers.QuestionMapper;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class QcmService {
	
	private final QcmRepository qcmRepository;
	private final QcmMapper qcmMapper;
	private final QuestionMapper questionMapper;
	private final QuestionRepository questionRepository;
	private final QuestionQcmRepository questionQcmRepository;
	private final QuestionQcmMapper questionQcmMapper;
	
	@Autowired
	public QcmService(QcmRepository qcmRepository, QcmMapper qcmMapper, QuestionMapper questionMapper, QuestionRepository questionRepository, QuestionQcmRepository questionQcmRepository, QuestionQcmMapper questionQcmMapper) {
		this.qcmRepository = qcmRepository;
		this.qcmMapper = qcmMapper;
		this.questionMapper = questionMapper;
		this.questionRepository = questionRepository;
		this.questionQcmRepository = questionQcmRepository;
		this.questionQcmMapper = questionQcmMapper;
	}
	
	public List<QcmDto> getAllQcm(){
		List<Qcm> qcmList = qcmRepository.findAll();
		return qcmList.stream()
				.map(qcmMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public QcmDto saveQcm(QcmDto qcmDto) {
		Qcm qcmEntity = qcmMapper.toEntity(qcmDto);
		Qcm savedQcmEntity = qcmRepository.save(qcmEntity);
		QcmDto resultDto = qcmMapper.toDto(savedQcmEntity);
		return resultDto;
	}
	
	public QuestionQcmDto updateOrder(QuestionQcmDto questionQcmDto) {
	    Optional<QuestionQcm> questionQcmOpt = questionQcmRepository.findById(questionQcmDto.getId());
	    
	    if (questionQcmOpt.isPresent()) {
	        QuestionQcm questionQcm = questionQcmOpt.get();
	        questionQcm.setOrdre(questionQcmDto.getOrdre());
	        questionQcmRepository.save(questionQcm);

	        return questionQcmMapper.toDto(questionQcm);
	    } else {
	        throw new EntityNotFoundException("No QuestionQcm found with id: " + questionQcmDto.getId());
	    }
	}

	@Transactional
	public void addQuestionToQcm(Long qcmId, Long questionId, int ordre) {
		Qcm qcm = qcmRepository.findById(qcmId)
				.orElseThrow(() -> new RuntimeException("Qcm not found with id: " + qcmId));
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

		QuestionQcm questionQcm = new QuestionQcm();
		questionQcm.setQcm(qcm);
		questionQcm.setQuestion(question);
		questionQcm.setOrdre(ordre);
		questionQcmRepository.save(questionQcm);
	}
	
	public List<QcmDto> getAllQcms() {
		List<Qcm> qcmList = qcmRepository.findAll();
		return qcmList.stream()
				.map(qcm -> {
					QcmDto qcmDto = qcmMapper.toDto(qcm);
					Set<QuestionQcm> questionQcms = qcm.getQuestions();
					qcmDto.setQuestions(questionQcms.stream().map(q -> {
						QuestionQcmDto questionQcmDto = new QuestionQcmDto();
						questionQcmDto.setId(q.getId());
						questionQcmDto.setQcmId(q.getQcm().getId());
						questionQcmDto.setQuestionIds(q.getQuestion().getId());
						questionQcmDto.setOrdre(q.getOrdre());
						return questionQcmDto;
					}).collect(Collectors.toList()));
					return qcmDto;
				}).collect(Collectors.toList());
	}

}
