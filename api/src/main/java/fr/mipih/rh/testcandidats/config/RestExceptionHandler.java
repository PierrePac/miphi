package fr.mipih.rh.testcandidats.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mipih.rh.testcandidats.dtos.ErreurDto;
import fr.mipih.rh.testcandidats.exceptions.AppException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(value = { AppException.class })
	@ResponseBody
	public ResponseEntity<ErreurDto> handleException(AppException ex) {
		return ResponseEntity.status(ex.getHttpStatus())
				.body(new ErreurDto(ex.getMessage()));
	}
}
