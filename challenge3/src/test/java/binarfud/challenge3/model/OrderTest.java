package binarfud.challenge3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    @DisplayName("Positive Test - order setter getter")
    void orderGetterTest(){
        Order order = new Order();

        order.setQty(1);
        order.setNotes("Pedes");

        Assertions.assertEquals(1, order.getQty());
        Assertions.assertEquals("Pedes", order.getNotes());
    }
}
