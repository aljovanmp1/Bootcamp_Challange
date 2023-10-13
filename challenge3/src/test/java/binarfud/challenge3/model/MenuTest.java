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
        menu.setId(1L);
        menu.setProductName("nasi goreng");
        menu.setPrice(10000);
        
        Merchant newMerchant = new Merchant();
        menu.setMerchant(newMerchant);

        Assertions.assertEquals(1L, menu.getId());
        Assertions.assertEquals("nasi goreng", menu.getProductName());
        Assertions.assertEquals(10000, menu.getPrice());
        Assertions.assertEquals(menu.getMerchant(), newMerchant);

    }
}
