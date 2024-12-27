package com.te.payment_service.exception;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String msg) {
		super(msg);
	}
}
