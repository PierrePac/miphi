package fr.mipih.rh.testcandidats.mappers;

import java.util.ArrayList;
import java.util.List;

import fr.mipih.rh.testcandidats.dtos.ReponseDto;
import fr.mipih.rh.testcandidats.models.Reponse;
import org.springframework.stereotype.Component;

@Component
public class ReponseMapper {
    public static ReponseDto toDto(Reponse reponse) {
        if (reponse == null) {
            return null;
        }

        ReponseDto dto = new ReponseDto();
        dto.setId(reponse.getId());
        dto.setReponse(reponse.getReponse());
        dto.setCorrect(reponse.isCorrect());

        if (reponse.getQuestion() != null) {
            dto.setQuestion_id(reponse.getQuestion().getId());
            // You may want to call your QuestionMapperManual here, assuming you have one
            dto.setQuestion(QuestionMapper.toDto(reponse.getQuestion()));
        }

        return dto;
    }

    public static Reponse toEntity(ReponseDto dto) {
        if (dto == null) {
            return null;
        }

        Reponse reponse = new Reponse();
        reponse.setId(dto.getId());
        reponse.setReponse(dto.getReponse());
        reponse.setCorrect(dto.isCorrect());

        if (dto.getQuestion() != null) {
            // You may want to call your QuestionMapperManual here, assuming you have one
            reponse.setQuestion(QuestionMapper.toEntity(dto.getQuestion()));
        }

        return reponse;
    }

    public static List<ReponseDto> toDtoList(List<Reponse> reponses) {
        List<ReponseDto> dtos = new ArrayList<>();
        for (Reponse reponse : reponses) {
            dtos.add(toDto(reponse));
        }
        return dtos;
    }

    public static List<Reponse> toEntityList(List<ReponseDto> dtos) {
        List<Reponse> reponses = new ArrayList<>();
        for (ReponseDto dto : dtos) {
            reponses.add(toEntity(dto));
        }
        return reponses;
    }
}
	

