package binarfud.challenge3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderDetailTest {
    
    OrderDetail orderDetail = new OrderDetail();

    @Test
    @DisplayName("Positive Test - order detail Getter")
    void testOrderDetailGetter(){
        Menu menu = new Menu();
        menu.setId(1L);
        OrderEntity order = new OrderEntity();
        order.setId(1L);

        orderDetail.setId(1L);
        orderDetail.setMenu(menu);
        orderDetail.setOrder(order);
        orderDetail.setQuantity(1);
        orderDetail.setTotalPrice(15000);

        Assertions.assertEquals(1L, orderDetail.getId());
        Assertions.assertEquals(1L, orderDetail.getOrder().getId());
        Assertions.assertEquals(1L, orderDetail.getOrder().getId());
        Assertions.assertEquals(1, orderDetail.getQuantity());
        Assertions.assertEquals(15000, orderDetail.getTotalPrice());
    }
}
