package binarfud.challenge3.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import binarfud.challenge3.utlis.Constants;

@SpringBootTest
public class StruckServiceTest {

    StruckService struckService = new StruckService();

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("Positive Test - get invoice")
    void getInvoiceTest() {
        String expectedResult = "";
        String sentence = "binarfud";

        expectedResult += Constants.NEWLINE;
        expectedResult += Constants.LINEWITHBREAK;
        expectedResult += sentence + Constants.NEWLINE;
        expectedResult += Constants.LINEWITHBREAK + Constants.NEWLINE;

        expectedResult += "Terima kasih sudah memesan\n";
        expectedResult += "di binarfud\n\n";

        expectedResult += "Dibawah ini adalah pesanan anda\n";

        expectedResult += orderDetails(2, 30000);
        expectedResult += "\nPembayaran : BinarCash\n";

        expectedResult += "\n========================\n";
        expectedResult += "Simpan struk ini sebagai\n";
        expectedResult += "bukti pembayaran\n";
        expectedResult += "========================\n\n";

        orderService.addOrder(1, 2);
        orderService.addNote(1, "Pedes");

        String actualResult = struckService.getInvoice(orderService.getOrderQty(), menuService.getMenuList(),
                orderService.getTotalItem(),
                orderService.getTotalPrice());

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Positive Test - save to file")
    void saveInvoiceToFileTest() {
        try {
            struckService.saveInvoiceToFile("Invoice cek");
        } catch (Exception e) {
            System.out.println("Save invoice to file failed:" + e.getMessage());
        }

        File f = new File(struckService.savePath);
        Assertions.assertTrue(f.exists() && !f.isDirectory());
    }

    String orderDetails(Integer totalItem, Integer totalPrice) {
        String result = "";
        StringBuilder bld = new StringBuilder();

        String resultFormat = "%-14s %-5s %s.000  %s%n";
        String menuName = "Nasi Goreng";
        int qty = 2;
        int price = 30;
        String note = "Pedes";

        String newLineData = String.format(resultFormat, menuName, qty, price, note);
        bld.append(newLineData);
        int resultLength = newLineData.length();

        result += bld.toString();

        bld.setLength(0);
        for (int i = 0; i < resultLength; i++) {
            bld.append("-");
        }

        result += bld.toString() + " +\n";
        result += String.format("%-14s %-5s %s.000 %n", "Total", totalItem, (totalPrice) / 1000);

        return result;
    }
}
