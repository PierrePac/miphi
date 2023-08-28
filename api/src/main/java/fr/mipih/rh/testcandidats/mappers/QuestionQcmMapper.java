package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.models.Qcm;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import fr.mipih.rh.testcandidats.models.QuestionQcmId;
import fr.mipih.rh.testcandidats.repositories.QcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionQcmRepository;
import fr.mipih.rh.testcandidats.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionQcmMapper {

    private final QcmMapper qcmMapper;
    private final QuestionMapper questionMapper;

    public QuestionQcmDto toDto(QuestionQcm questionQcm) {
        return new QuestionQcmDto(
                questionQcm.getQuestion().getId(),
                questionQcm.getQcm().getId(),
                questionQcm.getOrdre()
        );
    }

    public QuestionQcm toEntity(QuestionQcmDto questionQcmDto, QuestionRepository questionRepository, QcmRepository qcmRepository) {
        QuestionQcmId questionQcmId = new QuestionQcmId(questionQcmDto.getIdQcm(), questionQcmDto.getIdQuestion());
        Optional<Qcm> qcmOpt = qcmRepository.findById(questionQcmDto.getIdQcm());
        if(qcmOpt.isPresent()) {
            Qcm qcm = qcmOpt.get();

            Optional<Question> questionOpt = questionRepository.findById(questionQcmDto.getIdQuestion());
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();


                return new QuestionQcm(questionQcmId, qcm, question, questionQcmDto.getOrdre());
            }
        }
        return null;
    }
}
