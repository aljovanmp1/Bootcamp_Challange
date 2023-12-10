package binarfud.challenge7.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binarfud.challenge7.model.Order;
import binarfud.challenge7.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;

    public Order saveOrder(Order order, String dest, UUID user) {
        order.setOrderTime(LocalDateTime.now());
        order.setUserId(user);
        order.setDestinationOrder(dest);
        order.setCompleted(false);

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(UUID id){
        return orderRepository.findById(id);
    }
}