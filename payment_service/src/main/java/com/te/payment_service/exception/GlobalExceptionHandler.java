package com.te.payment_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.te.payment_service.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PaymentFailedException.class)
	public ResponseEntity<ApiResponse> paymentFailed(PaymentFailedException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ApiResponse> dataNotFound(DataNotFoundException e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

}