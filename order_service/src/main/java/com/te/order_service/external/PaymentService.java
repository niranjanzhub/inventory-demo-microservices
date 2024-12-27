package com.te.order_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.te.order_service.response.ApiResponse;

@FeignClient(name = "PAYMENT-SERVICE", url = "http://localhost:8083/payments")
public interface PaymentService {

	@PostMapping("/{orderId}")
	public ApiResponse  makePayment(@PathVariable int orderId);

	@PostMapping("/{paymentId}/{bank}")
	public ApiResponse confirmPayment(@PathVariable int paymentId, @PathVariable String bank);
	
	@GetMapping("/paymentByOrderId/{orderId}")
	public ApiResponse getPaymentDetail(@PathVariable int orderId);
	
	@PutMapping("/cancelpayment/{orderId}")
	public ApiResponse cancelPayment(@PathVariable int orderId);
}
