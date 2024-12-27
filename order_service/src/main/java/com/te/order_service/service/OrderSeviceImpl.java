package com.te.order_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.order_service.OrderRepository;
import com.te.order_service.dto.CreateOrder;
import com.te.order_service.dto.Inventory;
import com.te.order_service.dto.OrderDetails;
import com.te.order_service.dto.PaymentDetail;
import com.te.order_service.dto.Product;
import com.te.order_service.entity.Order;
import com.te.order_service.exception.DataNotFoundException;
import com.te.order_service.exception.OrderCancelException;
import com.te.order_service.external.InventoryService;
import com.te.order_service.external.PaymentService;
import com.te.order_service.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderSeviceImpl implements OrderService {

	private final RestTemplate restTemplate;
	private final InventoryService inventoryService;
	private final PaymentService paymentService;
	private final OrderRepository orderRepository;

	@Override
	public OrderDetails createOrder(CreateOrder createOrder) {
		// by rest template to find product
		ApiResponse productDetail = restTemplate.getForObject(
				"http://PRODUCT-SERVICE/products/getproduct/" + createOrder.getProductId(), ApiResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		Product product = mapper.convertValue(productDetail.getData(), Product.class);

		if (product != null && product.getStockAvailable() >= 1) {

			// by feign client to reserve stock
			ApiResponse stockResponse = inventoryService.reserverStock(createOrder.getProductId(),
					createOrder.getQuantity());
			Inventory inventory = mapper.convertValue(stockResponse.getData(), Inventory.class);
			if (stockResponse.isError() == false) {
				Order order = new Order();
				order.setProductId(product.getId());
				order.setStatus("pending");
				order.setPaymentStatus("pending");
				order.setQuantity(createOrder.getQuantity());
				OrderDetails orderDetails = new OrderDetails();
				BeanUtils.copyProperties(orderRepository.save(order), orderDetails);
				product.setStockAvailable(inventory.getStock());
				if (product.getStockAvailable() == 0) {
					product.setAvailable(false);
				}
				orderDetails.setProduct(product);
				orderDetails.setQuantity(createOrder.getQuantity());

				return orderDetails;

			}
			throw new DataNotFoundException("stock not available");
		}
		throw new OrderCancelException("product not present or not in stock");
	}

	@Override
	public OrderDetails makePayment(int orderID) {
		ApiResponse paymentResponse = paymentService.makePayment(orderID);
		ObjectMapper mapper = new ObjectMapper();
		PaymentDetail paymentDetail = mapper.convertValue(paymentResponse.getData(),
				new TypeReference<PaymentDetail>() {
				});
		if (paymentDetail != null) {
			Order order = orderRepository.findByOrderId(orderID);
			ApiResponse productDetail = restTemplate.getForObject(
					"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
			Product product = mapper.convertValue(productDetail.getData(), Product.class);
			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setStatus("pending");
			orderDetails.setPaymentStatus("pending");
			orderDetails.setOrderId(orderID);
			orderDetails.setProduct(product);
			orderDetails.setPaymentDetail(paymentDetail);
			orderDetails.setQuantity(order.getQuantity());

			return orderDetails;
		}

		throw new DataNotFoundException("order not found");
	}

	@Override
	public OrderDetails confirmPayment(int paymentId, String bank) {
		ApiResponse paymentResponse = paymentService.confirmPayment(paymentId, bank);
		ObjectMapper mapper = new ObjectMapper();
		PaymentDetail paymentDetail = mapper.convertValue(paymentResponse.getData(), PaymentDetail.class);
		if (paymentDetail != null && paymentDetail.getPaymentStatus().equalsIgnoreCase("success")) {
			Order order = orderRepository.findByOrderId(paymentDetail.getOrderId());
			ApiResponse productDetail = restTemplate.getForObject(
					"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
			Product product = mapper.convertValue(productDetail.getData(), Product.class);
			order.setStatus("Confirm");
			order.setPaymentStatus("success");
			orderRepository.save(order);
			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setStatus("confirm");
			orderDetails.setPaymentStatus("success");
			orderDetails.setOrderId(order.getOrderId());
			orderDetails.setPaymentDetail(paymentDetail);
			orderDetails.setQuantity(order.getQuantity());
			orderDetails.setProduct(product);

			return orderDetails;
		}
		throw new DataNotFoundException("payment detail not found or already payment done");
	}

	@Override
	public OrderDetails orderDetails(int orderId) {
		Order order = orderRepository.findByOrderId(orderId);
		if (order != null) {
			ApiResponse productDetail = restTemplate.getForObject(
					"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			Product product = mapper.convertValue(productDetail.getData(), Product.class);
			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setOrderId(orderId);
			orderDetails.setProduct(product);
			orderDetails.setQuantity(order.getQuantity());
			orderDetails.setStatus(order.getStatus());
			return orderDetails;
		}
		throw new DataNotFoundException("Order not found...");
	}

	@Override
	public OrderDetails cancelOrder(int orderId) {
		Order order = orderRepository.findByOrderId(orderId);
		if (order != null
				&& (order.getStatus().equalsIgnoreCase("Confirm") || order.getStatus().equalsIgnoreCase("pending"))) {
			if (order.getPaymentStatus().equalsIgnoreCase("success")) {

				inventoryService.releaseLock(order.getProductId(), order.getQuantity());
				ObjectMapper mapper = new ObjectMapper();
				// getting product details by restTemplate
				ApiResponse productDetail = restTemplate.getForObject(
						"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
				Product product = mapper.convertValue(productDetail.getData(), Product.class);
				orderRepository.delete(order);
				OrderDetails details = new OrderDetails();
				details.setOrderId(orderId);
				details.setProduct(product);
				details.setQuantity(order.getQuantity());
				details.setStatus("canceled");
				details.setPaymentStatus("canceled");
				// cancel payment
				ApiResponse cancelPayment = paymentService.cancelPayment(orderId);
				PaymentDetail paymentDetail = mapper.convertValue(cancelPayment.getData(), PaymentDetail.class);
				paymentDetail.setPaymentStatus("payment cancel Refund processed..");
				details.setPaymentDetail(paymentDetail);
				return details;
			} else {
				inventoryService.releaseLock(order.getProductId(), order.getQuantity());
				ObjectMapper mapper = new ObjectMapper();
				// getting product details by restTemplate
				ApiResponse productDetail = restTemplate.getForObject(
						"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
				Product product = mapper.convertValue(productDetail.getData(), Product.class);
				orderRepository.delete(order);
				OrderDetails details = new OrderDetails();
				details.setOrderId(orderId);
				details.setProduct(product);
				details.setQuantity(order.getQuantity());
				details.setStatus("canceled");
				details.setPaymentStatus("canceled");
				// cancel payment
				ApiResponse cancelPayment = paymentService.cancelPayment(orderId);
				PaymentDetail paymentDetail = mapper.convertValue(cancelPayment.getData(), PaymentDetail.class);
				paymentDetail.setPaymentStatus("payment cancel Refund processed..");
				details.setPaymentDetail(paymentDetail);
				return details;
			}

		}
		throw new DataNotFoundException("Order not found...");
	}

	@Override
	public OrderDetails deleteOrder(int orderId) {
		Order order = orderRepository.findByOrderId(orderId);
		System.err.println(order);
		if (order != null) {
			OrderDetails details = new OrderDetails();
			BeanUtils.copyProperties(order, details);
			ObjectMapper mapper = new ObjectMapper();
			if (order.getQuantity() >= 1) {
				inventoryService.releaseLock(order.getProductId(), order.getQuantity());
				ApiResponse productDetail = restTemplate.getForObject(
						"http://PRODUCT-SERVICE/products/getproduct/" + order.getProductId(), ApiResponse.class);
				Product product = mapper.convertValue(productDetail.getData(), Product.class);
				details.setProduct(product);
				orderRepository.delete(order);
				return details;
			} else {
				orderRepository.delete(order);
				return details;
			}
		}
		throw new DataNotFoundException("Order not found...");
	}

}
