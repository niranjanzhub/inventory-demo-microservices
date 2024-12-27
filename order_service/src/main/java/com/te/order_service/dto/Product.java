package com.te.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private Integer id;
	private String name;
	private Double price;
	private Boolean available;
	private int stockAvailable;

}
