package com.te.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.inventory_service.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer>  {
 
	Inventory findByProductId(Integer productId);
}
