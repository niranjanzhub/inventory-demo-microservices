package com.te.product_service.service;

import java.util.List;

import com.te.product_service.dto.ProductDto;

public interface ProductService {

	public ProductDto addProduct(ProductDto productDto);

	public ProductDto upadate(int id);

	public List<ProductDto> getAllAvailableProduct();

	public ProductDto getproductById(int id);
}
