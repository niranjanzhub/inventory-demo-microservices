package com.te.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
	
	private Integer id;
	@NotBlank
	private String name;
	@NotNull
	@PositiveOrZero(message = "price cannot be negative")
	private Double price;
	private Boolean available;
	@NotNull
	@PositiveOrZero(message = "Quantity cannot be negative")
	private int stockAvailable;
}
