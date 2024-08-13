package me.darshan.orderservice.service;

import jakarta.transaction.Transactional;
import me.darshan.adminservice.exceptions.ProductNotFoundException;
import me.darshan.orderservice.dto.OrderDTO;
import me.darshan.orderservice.exceptions.OrderNotFoundException;
import me.darshan.orderservice.model.Order;
import me.darshan.orderservice.model.OrderItem;
import me.darshan.orderservice.model.Product;
import me.darshan.orderservice.repository.OrderItemRepository;
import me.darshan.orderservice.repository.OrderRepository;
import me.darshan.orderservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());

        BigDecimal totalPrice = calculateTotalPrice(orderItems);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order); // Set the reference to the Order entity
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

            // Delete associated order items first
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setOrder(null); // Remove reference to order to avoid cascading delete
                orderItemRepository.delete(orderItem);
            });

            // Delete the order itself
            orderRepository.delete(order);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error deleting order with id " + id, e);
            throw e; // Rethrow the exception to trigger the 500 Internal Server Error
        }
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            Product product = productRepository.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + orderItem.getProduct().getId() + " not found"));

            if (product.getStock() < orderItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);

            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepository.save(product);
        }
        return totalPrice;
    }
}
