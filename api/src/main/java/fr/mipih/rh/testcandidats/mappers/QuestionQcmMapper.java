package fr.mipih.rh.testcandidats.mappers;

import fr.mipih.rh.testcandidats.dtos.QcmDto;
import fr.mipih.rh.testcandidats.dtos.QuestionDto;
import fr.mipih.rh.testcandidats.dtos.QuestionQcmDto;
import fr.mipih.rh.testcandidats.models.Question;
import fr.mipih.rh.testcandidats.models.QuestionQcm;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionQcmMapper {

    private final QcmMapper qcmMapper;
    private final QuestionMapper questionMapper;

    public QuestionQcmDto toDto(QuestionQcm questionQcm) {
        if (questionQcm == null) {
            return null;
        }

        QcmDto qcmDto = qcmMapper.toDto(questionQcm.getQcm());
        QuestionDto questionDto = questionMapper.toDto(questionQcm.getQuestion());

        return QuestionQcmDto.builder()
                .id(questionQcm.getId())
                .qcm(qcmDto)
                .question(questionDto)
                .ordre(questionQcm.getOrdre())
                .build();
    }

    public QuestionQcm toEntity(QuestionQcmDto questionQcmDto) {
        if (questionQcmDto == null) {
            return null;
        }

        QuestionQcm questionQcm = new QuestionQcm();
        questionQcm.setId(questionQcmDto.getId());
        questionQcm.setQcm(qcmMapper.toEntity(questionQcmDto.getQcm()));
        questionQcm.setQuestion(questionMapper.toEntity(questionQcmDto.getQuestion()));
        questionQcm.setOrdre(questionQcmDto.getOrdre());

        return questionQcm;
    }
}
