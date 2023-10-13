package binarfud.challenge3.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.Getter;
import binarfud.challenge3.model.Menu;
import binarfud.challenge3.model.Order;

@Service
public class OrderService {
    private final MenuService menuService;
    private @Getter LinkedHashMap<Long, Menu> menuList;

    public OrderService(MenuService menuService) {
        this.menuService = menuService;
        menuList = this.menuService.getMenuList();
    }


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

    public void addNote(Integer menuId, String note) {
        Order order = orderQty.get(menuId);
        order.setNotes(note);
    }

    public Integer getTotalItem() {
        return this.orderQty.values()
                .stream()
                .mapToInt(Order::getQty)
                .sum();
    }

    public Integer getTotalPrice() {
        if (menuList.size()==0) {
            menuService.initData();
            menuService.refetchData();
            menuList = this.menuService.getMenuList();
        }
        return this.orderQty.entrySet()
                .stream()
                .mapToInt(order -> {
                    Integer qty = order.getValue().getQty();
                    Integer price = menuList.get(Long.valueOf(order.getKey())).getPrice();
                    return qty * price;
                })
                .sum();
    }

    public static Integer getItemPriceTotal(Long menuId, Map<Integer, Order> orderQtyData, Map<Long, Menu> menuData) {
        Integer qty = orderQtyData.get(menuId.intValue()).getQty();
        Integer price = menuData.get(menuId).getPrice();
        return qty * price;
    }

    public void clearOrder(){
        orderQty.clear();
    }

}
