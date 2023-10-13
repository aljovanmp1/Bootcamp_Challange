package binarfud.challenge3.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import binarfud.challenge3.model.Menu;
import binarfud.challenge3.model.Order;
import binarfud.challenge3.view.View;

public class StruckService {
    String savePath = "./struk.txt";
    View view = new View();

    public void saveInvoiceToFile(String content) throws IOException {
        boolean isAppend = false;
        FileWriter writer = new FileWriter(this.savePath, isAppend);
        PrintWriter printLine = new PrintWriter(writer);

        printLine.print(content);
        printLine.close();
    }

    public String getInvoice(Map<Integer, Order> orderQty, Map<Long, Menu> menuList, Integer totalItem, Integer totalPrice) {
        String result = "";
        result += View.determineHeaderContent("binarfud");

        result += "Terima kasih sudah memesan\n";
        result += "di binarfud\n\n";

        result += "Dibawah ini adalah pesanan anda\n";

        result += view.formatOrderConfirmation(orderQty, menuList, totalItem, totalPrice);
        result += "\nPembayaran : BinarCash\n";

        result += "\n========================\n";
        result += "Simpan struk ini sebagai\n";
        result += "bukti pembayaran\n";
        result += "========================\n\n";

        return result;

    }

    
}
