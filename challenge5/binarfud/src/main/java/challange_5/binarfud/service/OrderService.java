package challange_5.binarfud.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challange_5.binarfud.model.Order;
import challange_5.binarfud.repository.OrderRepository;
import challange_5.binarfud.model.User;

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