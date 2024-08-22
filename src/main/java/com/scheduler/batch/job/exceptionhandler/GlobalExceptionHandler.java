package com.scheduler.batch.job.exceptionhandler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.common.artifact.ErrorResponse;
import com.common.artifact.ValidationError;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request){
		List<ValidationError> errors = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage())).toList();
	            

	        ErrorResponse errorResponse = new ErrorResponse("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
