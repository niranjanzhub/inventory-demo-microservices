package com.te.order_service.exception;

public class OrderCancelException extends RuntimeException {

	public OrderCancelException(String msg) {
		super(msg);
	}
}
