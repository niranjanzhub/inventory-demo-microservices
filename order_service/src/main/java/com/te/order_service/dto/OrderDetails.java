package com.te.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetails {

	private Integer orderId;
	private Integer quantity;
	private String status;
	private String paymentStatus;
	private Product product;
	private PaymentDetail paymentDetail;
}
