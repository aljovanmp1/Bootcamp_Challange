package challenge6.binarfud.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge6.binarfud.model.Order;
import challenge6.binarfud.repository.OrderRepository;
import challenge6.binarfud.model.User;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;

    public Order saveOrder(Order order, String dest) {
        List<User> users = userService.getAll();

        order.setOrderTime(LocalDateTime.now());
        order.setUser(users.get(0));
        order.setDestinationOrder(dest);
        order.setCompleted(false);

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(UUID id){
        return orderRepository.findById(id);
    }
}