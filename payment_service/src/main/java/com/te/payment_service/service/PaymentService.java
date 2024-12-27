package com.te.payment_service.service;

import java.util.List;

import com.te.payment_service.dto.PaymentDetail;

public interface PaymentService {

	public PaymentDetail makePayment(int orderId);

	public PaymentDetail confirmPayment(int paymentId, String bankAccount);

	public List<PaymentDetail> getPaymentStatus(String field);

	public PaymentDetail paymentByOrderId(int orderId);

	public PaymentDetail cancelPayment(int orderId);
}
