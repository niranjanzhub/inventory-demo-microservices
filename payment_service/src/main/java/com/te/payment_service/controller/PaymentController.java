package com.te.payment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.payment_service.dto.PaymentDetail;
import com.te.payment_service.response.ApiResponse;
import com.te.payment_service.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/{orderId}")
	public ResponseEntity<ApiResponse> makePayment(@PathVariable int orderId) {
		return ResponseEntity.ok(new ApiResponse(false, "payment pending...", paymentService.makePayment(orderId)));
	}

	@PostMapping("/{paymentId}/{bank}")
	public ResponseEntity<ApiResponse> confirmPayment(@PathVariable int paymentId, @PathVariable String bank) {
		return ResponseEntity
				.ok(new ApiResponse(false, "payment successfull...", paymentService.confirmPayment(paymentId, bank)));
	}

	@GetMapping("/paymentStatus/{field}")
	public ResponseEntity<ApiResponse> getPaymentStatus(@PathVariable String field) {
		return ResponseEntity.ok(new ApiResponse(false, "payment Status Fetched successfully...",
				paymentService.getPaymentStatus(field)));
	}
	@GetMapping("/paymentByOrderId/{orderId}")
	public ResponseEntity<ApiResponse> paymentByOrderId(@PathVariable int orderId) {
		return ResponseEntity.ok(new ApiResponse(false, "payment Fetched successfully...",
				paymentService.paymentByOrderId(orderId)));
	}
	
	@PutMapping("/cancelpayment/{orderId}")
	public ResponseEntity<ApiResponse> cancelPayment(@PathVariable int orderId) {
		return ResponseEntity.ok(new ApiResponse(false, "payment cancel successfully...",
				paymentService.cancelPayment(orderId)));
	}
	
}
