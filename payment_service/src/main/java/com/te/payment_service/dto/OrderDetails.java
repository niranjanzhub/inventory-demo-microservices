package com.te.payment_service.dto;

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
	private String status;
	private Integer quantity;
	private String paymentStatus;
	private Product product;
	private PaymentDetail paymentDetail;
}
