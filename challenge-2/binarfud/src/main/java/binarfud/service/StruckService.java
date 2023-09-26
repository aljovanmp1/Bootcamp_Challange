package binarfud.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import binarfud.model.Menu;
import binarfud.view.View;

public class StruckService {
    String savePath = "./struk.txt";

    public void saveInvoiceToFile(String content) throws IOException {
        boolean isAppend = false;
        FileWriter writer = new FileWriter(this.savePath, isAppend);
        PrintWriter printLine = new PrintWriter(writer);

        printLine.print(content);
        printLine.close();
    }

    public String getInvoice(Map<Integer, Integer> orderQty, Map<Integer, Menu> menuList) {
        String result = "";
        result += View.determineHeaderContent("BinarFud");

        result += "Terima kasih sudah memesan\n";
        result += "di BinarFud\n\n";

        result += "Dibawah ini adalah pesanan anda\n";

        result += View.formatOrderConfirmation(orderQty, menuList);
        result += "\nPembayaran : BinarCash\n";

        result += "\n========================\n";
        result += "Simpan struk ini sebagai\n";
        result += "bukti pembayaran\n";
        result += "========================\n\n";

        return result;

    }

    
}
