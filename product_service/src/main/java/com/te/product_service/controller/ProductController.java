package com.te.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.product_service.dto.ProductDto;
import com.te.product_service.response.ApiResponse;
import com.te.product_service.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService  productService;

	@PostMapping("/")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid ProductDto productDto){
		return ResponseEntity.ok(new ApiResponse(false, "Product added successfully", productService.addProduct(productDto)));
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable int id){
		return ResponseEntity.ok(new ApiResponse(false, "Product updated successfully", productService.upadate(id)));
	}
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllAvailableProduct(){
		return ResponseEntity.ok(new ApiResponse(false, "Product fetch successfully", productService.getAllAvailableProduct()));
	}
	@GetMapping("/getproduct/{id}")
	public ResponseEntity<ApiResponse> getproductById(@PathVariable int id){
		return ResponseEntity.ok(new ApiResponse(false, "Product fetch successfully", productService.getproductById(id)));
	}
	
	
}
