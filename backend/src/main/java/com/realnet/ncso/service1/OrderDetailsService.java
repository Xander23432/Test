package com.realnet.ncso.service1;

import java.util.List;
import java.util.Optional;

import com.realnet.ncso.entity1.OrderDetails;

public interface OrderDetailsService {
	public OrderDetails addOrder(OrderDetails orderDetails);
	public Optional<OrderDetails> getOrderById(Long id);
	public List<OrderDetails> getAllOrders();
	public OrderDetails updateOrder(OrderDetails orderDetails);
	void deleteOrder(Long id);
}
