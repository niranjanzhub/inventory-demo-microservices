package com.te.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetail {

	private Integer paymentId;
	private Integer orderId;
	private String paymentStatus;
	private Double paymentAmount;
}
