package challenge_4.binarfud.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge_4.binarfud.model.Order;
import challenge_4.binarfud.repository.OrderRepository;
import challenge_4.binarfud.model.User;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;

    public Order saveOrder(Order order, String dest) {
        List<User> users = userService.getAll();

        order.setOrderTime(LocalTime.now());
        order.setUser(users.get(0));
        order.setDestinationOrder(dest);
        order.setCompleted(false);

        return orderRepository.save(order);
    }
}