package com.te.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.inventory_service.dto.InventoryDto;
import com.te.inventory_service.response.ApiResponse;
import com.te.inventory_service.service.InventoryService;

import jakarta.ws.rs.PUT;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@PostMapping("/addstock/{productId}/{stock}")
	public ResponseEntity<ApiResponse> aaddStock(@PathVariable int productId, @PathVariable int stock) {
		return ResponseEntity.ok(new ApiResponse(false, "stock added", inventoryService.addStock(productId, stock)));
	}

	@GetMapping("/{productId}/{quantity}")
	public ResponseEntity<ApiResponse> reserverStock(@PathVariable int productId, @PathVariable int quantity) {
		return ResponseEntity.ok(new ApiResponse(false, "stock reserve", inventoryService.reserverStock(productId, quantity)));
	}

	@GetMapping("/getstock/{productId}")
	public ResponseEntity<ApiResponse> getStock(@PathVariable int productId) {
		return ResponseEntity.ok(new ApiResponse(false, "stock fetch successfully", inventoryService.getStock(productId)));
	}
	@PutMapping("/releaselock/{productId}/{quantity}")
	public ResponseEntity<ApiResponse> releaseLock(@PathVariable int productId,@PathVariable int quantity) {
		return ResponseEntity.ok(new ApiResponse(false, "stock released after cancel order..", inventoryService.releaseLock(productId, quantity)));
	}
}
