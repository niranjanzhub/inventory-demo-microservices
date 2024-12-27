package com.te.payment_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.payment_service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	Optional<Payment> findByPaymentId(Integer paymentId);

	Optional<Payment> findByOrderId(Integer orderId);
	
	
	List<Payment> findByPaymentStatus(String paymentStatus);
}
