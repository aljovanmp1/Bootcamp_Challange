package binarfud.challenge3.model;

import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderEntityTest {
    
    OrderEntity order = new OrderEntity();

    @Test
    @DisplayName("Positive Test - Order Getter")
    void testOrderGetter(){
        LocalTime ltime = LocalTime.now();
        User user = new User();
        user.setId(1L);

        order.setId(1L);
        order.setCompleted(true);
        order.setDestinationOrder("Jakarta");
        order.setOrderTime(ltime);
        order.setUser(user);

        Assertions.assertEquals(1L, order.getId());
        Assertions.assertEquals(true, order.getCompleted());
        Assertions.assertEquals("Jakarta", order.getDestinationOrder());
        Assertions.assertEquals(ltime, order.getOrderTime());
        Assertions.assertEquals(1L, order.getUser().getId());
    }
}
