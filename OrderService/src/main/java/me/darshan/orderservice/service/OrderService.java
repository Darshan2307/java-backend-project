package me.darshan.orderservice.service;


import me.darshan.orderservice.dto.OrderDTO;
import me.darshan.orderservice.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(OrderDTO orderDTO);

    void deleteOrder(Long id);
}
