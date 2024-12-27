package com.te.product_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.te.product_service.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ApiResponse> dataNotFound(DataNotFoundException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

	@ExceptionHandler(ProductFoundException.class)
	public ResponseEntity<ApiResponse> dataFound(ProductFoundException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArguments(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

}