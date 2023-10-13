package binarfud.challenge3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MerchantTest {
    
    Merchant merchant = new Merchant();

    @Test
    @DisplayName("Positive Test - Merchant Getter")
    void testMerchantGetter(){
        merchant.setId(1L);
        merchant.setMerchantLocation("Jakarta");
        merchant.setMerchantName("Binarfud");
        merchant.setOpen(true);

        Assertions.assertEquals(1L, merchant.getId());
        Assertions.assertEquals("Jakarta", merchant.getMerchantLocation());
        Assertions.assertEquals("Binarfud", merchant.getMerchantName());
        Assertions.assertEquals(true, merchant.getOpen());
    }
}
