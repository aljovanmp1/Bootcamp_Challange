package challenge_4.binarfud.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.OrderDetail;
import challenge_4.binarfud.model.Order;
import challenge_4.binarfud.repository.OrderDetailRepository;
import lombok.Getter;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    private @Getter LinkedHashMap<Long, OrderDetail> orderQty = new LinkedHashMap<>();
    

    public void addOrder(Long menuKey, Product menu, Integer qty) {
        if (!orderQty.containsKey(menuKey)) {
            OrderDetail newOrder = new OrderDetail();
            newOrder.setMenu(menu);
            newOrder.setQuantity(qty);
            newOrder.setTotalPrice(qty * menu.getPrice());
            orderQty.put(menuKey, newOrder);
        } else {
            OrderDetail order = orderQty.get(menuKey);
            order.setQuantity(qty);
            order.setTotalPrice(qty * menu.getPrice());
        }
    }

    public void addNote(Long menuKey, String note) {
        OrderDetail order = orderQty.get(menuKey);
        order.setNote(note);
    }

    public Integer getTotalItem() {
        return this.orderQty.values()
                .stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }

    public Integer getTotalPrice() {
        return this.orderQty.entrySet()
                .stream()
                .mapToInt(order -> order.getValue().getTotalPrice())
                .sum();
    }

    public void clearOrder() {
        orderQty.clear();
    }

    public void saveOrderDetail(Order order) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Map.Entry<Long, OrderDetail> entry : orderQty.entrySet()){
            entry.getValue().setOrder(order);
            orderDetailList.add(entry.getValue());
        }
        orderDetailRepository.saveAll(orderDetailList);
    }
}
