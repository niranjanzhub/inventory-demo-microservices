package com.te.inventory_service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.te.inventory_service.response.ApiResponse;

@FeignClient(name = "PRODUCT-SERVICE",url = "http://localhost:8080/products")
public interface ProductService {

	@PutMapping("/update/{id}")
	public ApiResponse updateProduct(@PathVariable int id);
}
