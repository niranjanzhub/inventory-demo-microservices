package com.te.product_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.product_service.dto.Inventory;
import com.te.product_service.dto.ProductDto;
import com.te.product_service.entity.Product;
import com.te.product_service.exception.DataNotFoundException;
import com.te.product_service.exception.ProductFoundException;
import com.te.product_service.external.InventoryService;
import com.te.product_service.repository.ProductRepository;
import com.te.product_service.response.ApiResponse;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private InventoryService inventoryService;

	@Override
	public ProductDto addProduct(ProductDto productDto) {
		Optional<Product> optionalProduct = productRepository.findByName(productDto.getName());
		if (!optionalProduct.isPresent()) {
			Product product = new Product();
			BeanUtils.copyProperties(productDto, product);
			Product dbProduct = productRepository.save(product);
			BeanUtils.copyProperties(dbProduct, productDto);
			inventoryService.addStock(dbProduct.getId(), productDto.getStockAvailable());
			return productDto;
		} else {
			Product product = optionalProduct.get();
			Integer id = product.getId();
			BeanUtils.copyProperties(productDto, product);
			product.setId(id);
			Product dbProduct = productRepository.save(product);
			BeanUtils.copyProperties(dbProduct, productDto);
			inventoryService.addStock(dbProduct.getId(), productDto.getStockAvailable());
			return productDto;

		}
	}

	@Override
	public ProductDto upadate(int id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		ApiResponse stockResponse = inventoryService.getStock(id);
		ObjectMapper mapper = new ObjectMapper();
		Inventory inventory = mapper.convertValue(stockResponse.getData(),Inventory.class);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			if (product.isAvailable() && inventory.getStock()==0) {
				product.setAvailable(false);
			} else {
				product.setAvailable(true);
			}

			ProductDto productDto = new ProductDto();
			BeanUtils.copyProperties(productRepository.save(product), productDto);
			return productDto;
		}
		throw new DataNotFoundException("product not found");
	}

	@Override
	public List<ProductDto> getAllAvailableProduct() {
		List<Product> productList = productRepository.findAll();
		List<Product> newProductList = productList.stream().filter(p -> p.isAvailable() == true)
				.collect(Collectors.toList());
		List<ProductDto> productDtoList = new ArrayList<>();
		if (!newProductList.isEmpty()) {
			for (Product product : newProductList) {
				ProductDto dto = new ProductDto();
				BeanUtils.copyProperties(product, dto);
				dto.setId(product.getId());
				productDtoList.add(dto);
			}
			return productDtoList;
		}
		throw new DataNotFoundException("product not found");
	}

	@Override
	public ProductDto getproductById(int id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Product not found"));
		ProductDto productDto = new ProductDto();
		if (product != null) {
			BeanUtils.copyProperties(product, productDto);
			ApiResponse stockResponse = inventoryService.getStock(id);
			ObjectMapper mapper = new ObjectMapper();
			Inventory inventory = mapper.convertValue(stockResponse.getData(),Inventory.class);
			productDto.setStockAvailable(inventory.getStock());
			return productDto;
		}
		throw new DataNotFoundException("product not available");
	}

}
