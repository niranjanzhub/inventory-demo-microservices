package com.te.order_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.te.order_service.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ApiResponse> dataNotFound(DataNotFoundException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

	@ExceptionHandler(OrderCancelException.class)
	public ResponseEntity<ApiResponse> orderCancel(OrderCancelException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}
}