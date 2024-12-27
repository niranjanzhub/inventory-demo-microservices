package com.te.order_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.te.order_service.response.ApiResponse;

@FeignClient(name = "INVENTORY-SERVICE", url = "http://localhost:8081/inventory/")
public interface InventoryService {

	@GetMapping("/{productId}/{quantity}")
	public ApiResponse reserverStock(@PathVariable int productId, @PathVariable int quantity);

	@PutMapping("/releaselock/{productId}/{quantity}")
	public ApiResponse releaseLock(@PathVariable int productId, @PathVariable int quantity);

}
