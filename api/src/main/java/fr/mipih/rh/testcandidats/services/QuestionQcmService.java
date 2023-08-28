package fr.mipih.rh.testcandidats.services;

import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.mappers.QuestionQcmMapper;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionQcmService {

    private final QuestionQcmRepository questionQcmRepository;
    private final QuestionQcmMapper questionQcmMapper;

    public QuestionQcmDto updateOrder(QuestionQcmDto questionQcmDto) {
        QuestionQcmId questionQcmId = new QuestionQcmId();
        questionQcmId.setIdQcm(questionQcmDto.getIdQcm());
        questionQcmId.setIdQuestion(questionQcmDto.getIdQuestion());
        Optional<QuestionQcm> questionQcmOpt = questionQcmRepository.findById(questionQcmId);

        if (questionQcmOpt.isPresent()) {
            QuestionQcm questionQcm = questionQcmOpt.get();
            questionQcm.setOrdre(questionQcmDto.getOrdre());
            questionQcmRepository.save(questionQcm);

            return questionQcmMapper.toDto(questionQcm);
        } else {
            throw new EntityNotFoundException("No QuestionQcm found with id: " + questionQcmId);
        }
    }
}
