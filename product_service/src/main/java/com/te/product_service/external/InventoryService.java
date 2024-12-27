package com.te.product_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.te.product_service.response.ApiResponse;

@FeignClient(name = "INVENTORY-SERVICE", url = "http://localhost:8081/inventory")
public interface InventoryService {

	@PostMapping("/addstock/{productId}/{stock}")
	public ApiResponse addStock(@PathVariable int productId, @PathVariable int stock);

	@GetMapping("/getstock/{productId}")
	public ApiResponse getStock(@PathVariable int productId);

}
