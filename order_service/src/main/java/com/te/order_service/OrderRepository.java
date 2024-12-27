package com.te.order_service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.order_service.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Order findByOrderId(Integer orderId);
}
