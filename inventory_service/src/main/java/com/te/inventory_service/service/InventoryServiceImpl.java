package com.te.inventory_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.inventory_service.dto.InventoryDto;
import com.te.inventory_service.entity.Inventory;
import com.te.inventory_service.exception.DataNotFoundException;
import com.te.inventory_service.external.ProductService;
import com.te.inventory_service.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ProductService productService;

	@Override
	public InventoryDto addStock(int productId, int stock) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory != null) {
			inventory.setStock(stock);
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventoryRepository.save(inventory), inventoryDto);
			return inventoryDto;
		} else {
			Inventory inventory1 = new Inventory();
			inventory1.setProductId(productId);
			inventory1.setStock(stock);
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventoryRepository.save(inventory1), inventoryDto);
			return inventoryDto;
		}
	}

	@Override
	public InventoryDto reserverStock(int productId, int quantity) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory != null && inventory.getStock() >= quantity) {
			if (quantity != 0) {
				inventory.setStock(inventory.getStock() - quantity);
				InventoryDto inventoryDto = new InventoryDto();
				BeanUtils.copyProperties(inventoryRepository.save(inventory), inventoryDto);
				Integer stock = inventory.getStock();
				if (stock == 0) {
					productService.updateProduct(productId);
				}
				return inventoryDto;
			}else {
				throw new DataNotFoundException("your cart is empty please enter product quantity");
			}
		}

		throw new DataNotFoundException("stock not available ");
	}

	@Override
	public InventoryDto getStock(int productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory != null) {
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventory, inventoryDto);
			return inventoryDto;
		}
		throw new DataNotFoundException("stock not found");
	}

	@Override
	public InventoryDto releaseLock(int productId, int quantity) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory != null && inventory.getStock() >= 0) {
			inventory.setStock(inventory.getStock() + quantity);
			InventoryDto inventoryDto = new InventoryDto();
			BeanUtils.copyProperties(inventoryRepository.save(inventory), inventoryDto);
			if (inventory.getStock() > 0) {
				productService.updateProduct(productId);
			}
			return inventoryDto;
		}
		throw new DataNotFoundException("stock not available");
	}

}
