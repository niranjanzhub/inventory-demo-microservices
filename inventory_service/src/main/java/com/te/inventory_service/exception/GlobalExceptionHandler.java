package com.te.inventory_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.te.inventory_service.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ApiResponse> dataNotFound(DataNotFoundException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

}