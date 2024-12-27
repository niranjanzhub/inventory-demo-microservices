package com.te.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.order_service.dto.CreateOrder;
import com.te.order_service.response.ApiResponse;
import com.te.order_service.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/")
	@CircuitBreaker(name = "productInventoryService", fallbackMethod = "productInventoryFault")
	public ResponseEntity<ApiResponse> createOrder(@RequestBody CreateOrder createOrder) {
		return ResponseEntity.ok(new ApiResponse(false, "Order is in Pending...", orderService.createOrder(createOrder)));
	}
     
	//method will get executed when createOrder is failed
	public ResponseEntity<ApiResponse> productInventoryFault(CreateOrder createOrder, Exception e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

	@PostMapping("/makepayment/{orderId}")
	@Retry(name = "paymentService", fallbackMethod = "payemntServiceFault")
	public ResponseEntity<ApiResponse> makePayment(@PathVariable int orderId) {
		return ResponseEntity.ok(new ApiResponse(false, "Order still in pending do payment now...", orderService.makePayment(orderId)));
	}
	@PostMapping("/confirmpayment/{paymentId}/{bank}")
	@Retry(name = "paymentService", fallbackMethod = "payemntServiceFault")
	public ResponseEntity<ApiResponse> makePayment(@PathVariable int paymentId,@PathVariable String bank) {
		return ResponseEntity.ok(new ApiResponse(false, "Order successfully placed...", orderService.confirmPayment(paymentId, bank)));
	}

	//method will get executed when makePayment or confirmPayment is failed
	public ResponseEntity<ApiResponse> payemntServiceFault(int orderId, String bank, Exception e) {
		return ResponseEntity.badRequest().body(new ApiResponse(true, e.getMessage(), null));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse> orderDetails(@PathVariable int orderId) {
		return ResponseEntity.ok(new ApiResponse(false, "Order Details Found..", orderService.orderDetails(orderId)));
	}

	@PutMapping("/cancel/{orderId}")
	@CircuitBreaker(name = "productInventoryService", fallbackMethod = "productInventoryFault")
	public ResponseEntity<ApiResponse> cancelOrder(@PathVariable int orderId) {
		return ResponseEntity
				.ok(new ApiResponse(false, "Order Cancel Successfully..", orderService.cancelOrder(orderId)));
	}
	
	@DeleteMapping("/deleteorder/{orderId}")
	public ResponseEntity<ApiResponse> deleteOrder(@PathVariable int orderId) {
		return ResponseEntity
				.ok(new ApiResponse(false, "Order Deleted Successfully..", orderService.deleteOrder(orderId)));
	}
}
