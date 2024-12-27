package com.te.payment_service.exception;

public class PaymentFailedException extends RuntimeException {

	public PaymentFailedException(String msg) {
		super(msg);
	}
}
