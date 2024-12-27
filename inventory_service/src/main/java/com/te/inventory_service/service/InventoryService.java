package com.te.inventory_service.service;

import com.te.inventory_service.dto.InventoryDto;

public interface InventoryService {

	public InventoryDto addStock(int productId, int stock);

	public InventoryDto reserverStock(int productId, int quantity);

	public InventoryDto getStock(int productId);
	
	public InventoryDto releaseLock(int productId,int quantity);
}
