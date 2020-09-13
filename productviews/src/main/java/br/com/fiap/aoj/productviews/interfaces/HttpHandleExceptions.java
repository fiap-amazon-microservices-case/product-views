package br.com.fiap.aoj.productviews.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
class HttpHandleExceptions {

	@ResponseStatus(value = NO_CONTENT)
	@ExceptionHandler(IllegalArgumentException.class)
	public void notContentHandle() {}

	@ResponseStatus(value = UNPROCESSABLE_ENTITY)
	@ExceptionHandler(RuntimeException.class)
	public void unprocesableEntityHandle() {}

	@ResponseStatus(value = BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<Map<String, Object>>> badRequestHandle(final MethodArgumentNotValidException methodArgumentNotValidException) {
		final List<Map<String, Object>> summarizedErrors = methodArgumentNotValidException.getBindingResult().getAllErrors()
				.stream() //
				.map(objectError -> { return (FieldError) objectError; })
				.map(this::mapSumarizedError) //
				.collect(Collectors.toList());

		return ResponseEntity.badRequest().body(summarizedErrors);
	}

	private Map<String, Object> mapSumarizedError(final FieldError fieldError) {
		final Map<String, Object> summarizedError = new HashMap<>();
		summarizedError.put("message", fieldError.getDefaultMessage());
		summarizedError.put("objectName", fieldError.getObjectName());
		summarizedError.put("objectName.field", fieldError.getField());
		summarizedError.put("rejectedValue", fieldError.getRejectedValue());

		return summarizedError;
	}
}