package binarfud.challenge8.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import binarfud.challenge8.dto.order.others.ArrangeOrderRespDto;
import binarfud.challenge8.dto.order.request.AddOrderDto;
import binarfud.challenge8.dto.order.request.AddOrderDto.OrderData;
import binarfud.challenge8.model.Order;
import binarfud.challenge8.model.OrderDetail;
import binarfud.challenge8.model.Product;
import binarfud.challenge8.model.User;
import binarfud.challenge8.repository.OrderRepository;
import binarfud.challenge8.utlis.DataNotFoundException;
import binarfud.challenge8.utlis.QtyException;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderDetailService orderDetailService;

    public Order saveOrder(Order order, String dest, UUID user) {
        order.setOrderTime(LocalDateTime.now());
        order.setUserId(user);
        order.setDestinationOrder(dest);
        order.setCompleted(false);

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    @Transactional(rollbackFor = { QtyException.class })
    public ArrangeOrderRespDto arrangeOrder(List<OrderData> orderDatas, String dest, User user)
            throws DataNotFoundException, QtyException {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (AddOrderDto.OrderData singleOrder : orderDatas) {
            Optional<Product> productOptional = productService.getProductById(singleOrder.getProductId());
            if (!productOptional.isPresent())
                throw new DataNotFoundException("Product id: " + singleOrder.getProductId() + " not found");

            Product selecteProduct = productOptional.get();
            OrderDetail newOrder = new OrderDetail();
            newOrder.setNote(singleOrder.getNote());
            newOrder.setProduct(selecteProduct);
            newOrder.setQuantity(singleOrder.getQuantity());
            newOrder.setTotalPrice(productOptional.get().getPrice() * singleOrder.getQuantity());

            if (singleOrder.getQuantity() > selecteProduct.getStock())
                throw new QtyException(selecteProduct.getProductName() + " requested qty: "
                        + singleOrder.getQuantity() + " available qty: " + selecteProduct.getStock());

            productService.decreaseStock(selecteProduct, singleOrder.getQuantity());

            orderDetailList.add(newOrder);
        }

        Order order = new Order();
        order = saveOrder(order, dest, user.getId());

        ArrangeOrderRespDto arrangedData = new ArrangeOrderRespDto();

        arrangedData.setOrder(order);
        arrangedData.setOrderDetailList(orderDetailList);

        return arrangedData;
    }
}