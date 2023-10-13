package binarfud.challenge3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

import binarfud.challenge3.model.Order;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void init() {
        orderService.clearOrder();
    }

    @Test
    @DisplayName("Positive Test - getter")
    void getMenuTest() {
        orderService.getOrderQty();
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Positive Test - add order")
    void addOrderTest() {
        orderService.addOrder(1, 10);
        orderService.addOrder(1, 2);

        LinkedHashMap<Integer, Order> orderQty = orderService.getOrderQty();
        assertEquals(orderQty.get(1).getQty(), 2);
    }

    @Test
    @DisplayName("Positive Test - add note")
    void addNoteTest() {
        orderService.addOrder(1, 10);
        orderService.addNote(1, "Pedes");

        LinkedHashMap<Integer, Order> orderQty = orderService.getOrderQty();
        assertEquals(orderQty.get(1).getNotes(), "Pedes");
    }

    @Test
    @DisplayName("Positive Test - get total item")
    void getTotalItemTest() {
        assertEquals(0, orderService.getTotalItem());
        orderService.addOrder(1, 2);
        orderService.addOrder(1, 3);
        assertEquals(3, orderService.getTotalItem());
    }

    @Test
    @DisplayName("Positive Test - get total price")
    void getTotalPriceTest() {
        assertEquals(0, orderService.getTotalPrice());
        orderService.addOrder(1, 2);
        orderService.addOrder(2, 2);
        assertEquals(56000, orderService.getTotalPrice());
    }

    @Test
    @DisplayName("Negative Test - get total price negative")
    void getNegTotalPriceTest() {
        orderService.getMenuList().clear();

        assertEquals(0, orderService.getTotalPrice());
        orderService.addOrder(1, 2);
        orderService.addOrder(2, 2);
        assertEquals(56000, orderService.getTotalPrice());
    }

    @Test
    @DisplayName("Positive Test - get item price")
    void getItemPriceTotalTest() {
        orderService.addOrder(1, 2);
        Integer itemPrice = OrderService.getItemPriceTotal(1L, orderService.getOrderQty(), menuService.getMenuList());
        assertEquals(30000, itemPrice);
    }

}
