package com.te.payment_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.te.payment_service.response.ApiResponse;

@FeignClient(name = "ORDER-SERVICE", url = "http://localhost:8082/orders/")
public interface OrderService {

	@GetMapping("/{orderId}")
	public ApiResponse getOrder(@PathVariable int orderId);

	@DeleteMapping("/deleteorder/{orderId}")
	public ApiResponse deleteOrder(@PathVariable int orderId);
}
