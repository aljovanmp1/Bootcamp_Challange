package binarfud.service;

import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;

import binarfud.model.Order;

@NoArgsConstructor
public class OrderService {
    private @Getter LinkedHashMap<Integer, Order> orderQty = new LinkedHashMap<>();

    public void addOrder(Integer menuId, Integer qty) {
        if (!orderQty.containsKey(menuId)) {
            Order newOrder = new Order();
            newOrder.setQty(qty);
            orderQty.put(menuId, newOrder);
        } else {
            Order order = orderQty.get(menuId);
            order.setQty(qty);
        }
    }

    public void addNote(Integer menuId, String note){
        Order order = orderQty.get(menuId);
        order.setNotes(note);
    }

}
