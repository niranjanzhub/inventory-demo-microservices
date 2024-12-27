package com.te.order_service.service;

import com.te.order_service.dto.CreateOrder;
import com.te.order_service.dto.OrderDetails;

public interface OrderService {

	public OrderDetails createOrder(CreateOrder createOrder);

	public OrderDetails makePayment(int orderId);

	public OrderDetails confirmPayment(int paymentId, String bank);

	public OrderDetails orderDetails(int orderId);

	public OrderDetails cancelOrder(int orderId);

	public OrderDetails deleteOrder(int orderId);
}
