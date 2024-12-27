package com.te.payment_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.payment_service.dto.OrderDetails;
import com.te.payment_service.dto.PaymentDetail;
import com.te.payment_service.dto.Product;
import com.te.payment_service.entity.Payment;
import com.te.payment_service.exception.DataNotFoundException;
import com.te.payment_service.exception.PaymentFailedException;
import com.te.payment_service.external.OrderService;
import com.te.payment_service.repository.PaymentRepository;
import com.te.payment_service.response.ApiResponse;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public PaymentDetail makePayment(int orderId) {

		// by feign client calling
		ApiResponse orderDetails = orderService.getOrder(orderId);
		ObjectMapper mapper = new ObjectMapper();
		OrderDetails order = mapper.convertValue(orderDetails.getData(), OrderDetails.class);
		if (order != null) {
			Product product = order.getProduct();
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetail.setOrderId(orderId);
			paymentDetail.setPaymentStatus("pending");
			paymentDetail.setPaymentAmount(product.getPrice() * order.getQuantity());
			Payment payment = new Payment();
			BeanUtils.copyProperties(paymentDetail, payment);
			BeanUtils.copyProperties(paymentRepository.save(payment), paymentDetail);
			return paymentDetail;
		}
		throw new DataNotFoundException("order not present");
	}

	@Override
	public PaymentDetail confirmPayment(int paymentId, String bankAccount) {

		Optional<Payment> optionalPayment = paymentRepository.findByPaymentId(paymentId);
		System.err.println(optionalPayment.get());
		if (optionalPayment.isPresent()) {
			System.err.println("enter if block");

			Map<String, Double> bank = new HashMap<>();
			bank.put("PNB", 138664.64);
			bank.put("SBI", 437364.64);
			bank.put("ICICI", 5364.64);
			bank.put("HDFC", 10364.64);

			Payment payment = optionalPayment.get();

			if (bank.get(bankAccount) > payment.getPaymentAmount()) {
				payment.setPaymentStatus("success");
				PaymentDetail paymentDetail = new PaymentDetail();
				BeanUtils.copyProperties(paymentRepository.save(payment), paymentDetail);
				return paymentDetail;
			} else {
				cancelPayment(payment.getOrderId());
				orderService.deleteOrder(payment.getOrderId());
				throw new PaymentFailedException("payment failed due to insufficient balance....");
			}
		}
		throw new DataNotFoundException("Payment details not found..");
	}

	@Override
	public List<PaymentDetail> getPaymentStatus(String field) {
		List<Payment> paymentList = paymentRepository.findByPaymentStatus(field);
		if (!paymentList.isEmpty()) {
			List<PaymentDetail> paymentDetailList = new ArrayList<>();
			for (Payment payment : paymentList) {
				PaymentDetail detail = new PaymentDetail();
				BeanUtils.copyProperties(payment, detail);
				paymentDetailList.add(detail);

			}
			return paymentDetailList;
		}
		throw new DataNotFoundException("Payment Status not present..");
	}

	@Override
	public PaymentDetail paymentByOrderId(int orderId) {
		Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
		if (optionalPayment.isPresent()) {
			PaymentDetail paymentDetail = new PaymentDetail();
			BeanUtils.copyProperties(optionalPayment.get(), paymentDetail);
			return paymentDetail;
		}
		throw new DataNotFoundException("Order not present");
	}

	@Override
	public PaymentDetail cancelPayment(int orderId) {
		Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
		if (optionalPayment.isPresent()) {
			PaymentDetail paymentDetail = new PaymentDetail();
			BeanUtils.copyProperties(optionalPayment.get(), paymentDetail);
			paymentRepository.delete(optionalPayment.get());
			return paymentDetail;
		}
		throw new DataNotFoundException("Order not present");
	}

}
