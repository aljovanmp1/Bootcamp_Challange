package binarfud.challenge3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import binarfud.challenge3.model.Menu;

public class MenuTest {
    @Test
    @DisplayName("Positive Test - menu getter")
    void menuGetterTest(){
        Menu menu = new Menu();

        Assertions.assertEquals(1, menu.getId());
        Assertions.assertEquals("nasi goreng", menu.getProductName());
        Assertions.assertEquals(10000, menu.getPrice());

    }
}
