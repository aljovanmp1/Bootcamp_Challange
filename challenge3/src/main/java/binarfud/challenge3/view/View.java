package binarfud.challenge3.view;

import java.util.Map;
import lombok.NoArgsConstructor;

import binarfud.challenge3.model.Menu;
import binarfud.challenge3.model.Order;
import binarfud.challenge3.service.OrderService;
import binarfud.challenge3.utlis.Constants;

@NoArgsConstructor
public class View {

    public void printMenu(Map<Long, Menu> menuList){
        printHeader("menu");
        int ind = 1;
        System.out.println("Silahkan pilih makanan : ");
        for (Map.Entry<Long, Menu> entry : menuList.entrySet()) {
            Menu menu = entry.getValue();
            System.out.printf("%d. %-13s| %d.000%n", ind, menu.getProductName(), menu.getPrice() / 1000);
            ind++;
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar Aplikasi");
        System.out.println();
    }

    public void printSelectedMenu(Menu selectedMenu) {
        printHeader("menuSelected");
        System.out.printf("%-13s| %d.000%n", selectedMenu.getProductName(), selectedMenu.getPrice() / 1000);
        System.out.println("(input 0 untuk kembali)");
        System.out.println();
    }

    public void printConfirmation(Map<Integer, Order> orderQty, Map<Long, Menu> menuList, Integer totalItem, Integer totalPrice){
        printHeader("confirmation");
        System.out.print(formatOrderConfirmation(orderQty, menuList, totalItem, totalPrice));

        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.println();
        
    }

    public String formatOrderConfirmation(Map<Integer, Order> orderQty, Map<Long, Menu> menuList, Integer totalItem, Integer totalPrice) {
        String result = "";

        StringBuilder bld = new StringBuilder();
        int longestOrder = 0;

        for (Map.Entry<Integer, Order> entry : orderQty.entrySet()) {
            
            Integer key = entry.getKey();
            Order order = entry.getValue();
            Integer qty = order.getQty();
            String note = order.getNotes();
            String menuName = menuList.get(Long.valueOf(key)).getProductName();

            Integer price = OrderService.getItemPriceTotal(Long.valueOf(key), orderQty, menuList) / 1000;
            String resultFormat = "%-14s %-5s %s.000  %s%n";
            
            
            String newLineData = String.format(resultFormat, menuName, qty, price, note);
            bld.append(newLineData);
            
            int resultLength = newLineData.length();
            longestOrder = resultLength>longestOrder ? resultLength : longestOrder;

        }
        result+= bld.toString();

        bld.setLength(0);
        for (int i = 0; i <longestOrder; i++) {
            bld.append("-");
        }

        if (longestOrder == 0) result+= "--------------------------- +\n";
        else result += bld.toString() + " +\n";

        result += String.format("%-14s %-5s %s.000 %n", "Total", totalItem, (totalPrice) / 1000);

        return result;
    }

    private void printHeader(String state) {
        switch (state) {
            case "menu":
                System.out.print(determineHeaderContent("Selamat datang di binarfud"));
                break;
            case "menuSelected":
                System.out.print(determineHeaderContent("Berapa pesanan anda"));
                break;
            case "confirmation":
                System.out.print(determineHeaderContent("Konfirmasi & Pembayaran"));
                break;
            case "invoice":
                System.out.print(determineHeaderContent("binarfud"));
                break;
            default:
                break;
        }
    }

    public static String determineHeaderContent(String sentence) {
        String result = "";
        result += Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += sentence + Constants.NEWLINE;
        result += Constants.LINEWITHBREAK + Constants.NEWLINE;
        return result;
    }

    public void printSubmitNote(){
        System.out.print("Masukkan Keterangan Tambahan => ");
    }

    public void printError(String state){
        String sentence = "";
        String footer = "";
        switch(state) {
            case Constants.WRONGINPUT:
                sentence+= "Mohon masukkan input\n";
                sentence+= "pilihan anda";
                footer+= "(Y) untuk lanjut\n";
                footer+= "(n) untuk keluar";
                break;
            case Constants.EMPTYORDER:
                sentence+= "Minimal 1 jumlah\n";
                sentence+= "pesanan!";
                break;
            default:
                break;
        }

        String result = "";
        result += Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += sentence + Constants.NEWLINE;
        result += Constants.LINEWITHBREAK;
        result += footer + Constants.NEWLINE;
        
        System.out.print(result);
    }
}
