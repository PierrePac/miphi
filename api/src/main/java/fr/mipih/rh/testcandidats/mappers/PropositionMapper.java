package fr.mipih.rh.testcandidats.mappers;

import java.util.ArrayList;
import java.util.List;

import fr.mipih.rh.testcandidats.dtos.PropositionDto;
import fr.mipih.rh.testcandidats.models.Proposition;
import org.springframework.stereotype.Component;

@Component
public class PropositionMapper {
    public static PropositionDto toDto(Proposition proposition) {
        if (proposition == null) {
            return null;
        }

        PropositionDto dto = new PropositionDto();
        dto.setId(proposition.getId());
        dto.setReponse(proposition.getReponse());
        dto.setCorrect(proposition.isCorrect());

        if (proposition.getQuestion() != null) {
            dto.setQuestion_id(proposition.getQuestion().getId());
        }

        return dto;
    }

    public static Proposition toEntity(PropositionDto dto) {
        if (dto == null) {
            return null;
        }

        Proposition proposition = new Proposition();
        proposition.setId(dto.getId());
        proposition.setReponse(dto.getReponse());
        proposition.setCorrect(dto.isCorrect());

        if (dto.getQuestion() != null) {
            proposition.setQuestion(QuestionMapper.toEntity(dto.getQuestion()));
        }

        return proposition;
    }

    public static List<PropositionDto> toDtoList(List<Proposition> propositions) {
        List<PropositionDto> dtos = new ArrayList<>();
        for (Proposition proposition : propositions) {
            dtos.add(toDto(proposition));
        }
        return dtos;
    }

    public static List<Proposition> toEntityList(List<PropositionDto> dtos) {
        List<Proposition> propositions = new ArrayList<>();
        for (PropositionDto dto : dtos) {
            propositions.add(toEntity(dto));
        }
        return propositions;
    }

    public static PropositionDto toGetAllDto(Proposition proposition) {
        if (proposition == null) {
            return null;
        }

        PropositionDto dto = new PropositionDto();
        dto.setId(proposition.getId());
        dto.setReponse(proposition.getReponse());
        dto.setCorrect(proposition.isCorrect());

        if (proposition.getQuestion() != null) {
            dto.setQuestion_id(proposition.getQuestion().getId());
        }

        return dto;
    }
}
	

