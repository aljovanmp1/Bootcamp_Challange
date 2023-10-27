package challenge_4.binarfud.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.stereotype.Service;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.view.ProductView;
import challenge_4.binarfud.view.View;
import challenge_4.binarfud.model.OrderDetail;

@Service
public class StruckService {
    String savePath = "./struk.txt";

    public void saveInvoiceToFile(String content) throws IOException {
        boolean isAppend = false;
        FileWriter writer = new FileWriter(this.savePath, isAppend);
        PrintWriter printLine = new PrintWriter(writer);

        printLine.print(content);
        printLine.close();
    }

    public String getInvoice(Map<Long, OrderDetail> orderQty, Map<Long, Product> menuList, Integer totalItem, Integer totalPrice) {
        String result = "";
        result += View.determineHeaderContent("binarfud");

        result += "Terima kasih sudah memesan\n";
        result += "di binarfud\n\n";

        result += "Dibawah ini adalah pesanan anda\n";

        result += ProductView.formatOrderConfirmation(orderQty, menuList, totalItem, totalPrice);
        result += "\nPembayaran : BinarCash\n";

        result += "\n========================\n";
        result += "Simpan struk ini sebagai\n";
        result += "bukti pembayaran\n";
        result += "========================\n\n";

        return result;

    }

    
}
