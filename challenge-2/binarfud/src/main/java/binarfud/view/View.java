package binarfud.view;

import java.util.Map;
import lombok.NoArgsConstructor;

import binarfud.model.Menu;
import binarfud.utlis.Constants;

@NoArgsConstructor
public class View {

    public void printMenu(Map<Integer, Menu> menuList){
        printHeader("menu");
        int ind = 1;
        System.out.println("Silahkan pilih makanan : ");
        for (Map.Entry<Integer, Menu> entry : menuList.entrySet()) {
            Menu menu = entry.getValue();
            System.out.printf("%d. %-13s| %d.000%n", ind, menu.getName(), menu.getPrice() / 1000);
            ind++;
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar Aplikasi");
        System.out.println();
    }

    public void printSelectedMenu(Menu selectedMenu) {
        printHeader("menuSelected");
        System.out.printf("%-13s| %d.000%n", selectedMenu.getName(), selectedMenu.getPrice() / 1000);
        System.out.println("(input 0 untuk kembali)");
        System.out.println();
    }

    public void printConfirmation(Map<Integer, Integer> orderQty, Map<Integer, Menu> menuList){
        printHeader("confirmation");
        System.out.print(formatOrderConfirmation(orderQty, menuList));

        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.println();
    }

    public static String formatOrderConfirmation(Map<Integer, Integer> orderQty, Map<Integer, Menu> menuList) {
        String result = "";
        int totalItem = 0;
        int totalPrice = 0;

        StringBuilder bld = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : orderQty.entrySet()) {
            
            Integer key = entry.getKey();
            Integer qty = entry.getValue();
            String menuName = menuList.get(key).getName();
            
            Integer price = qty * menuList.get(key).getPrice() / 1000;
            String resultFormat = "%-14s %-5s %s.000 %n";
            
            bld.append(String.format(resultFormat, menuName, qty, price));

            totalItem += orderQty.get(key);
            totalPrice += orderQty.get(key) * menuList.get(key).getPrice();
        }
        result+= bld.toString();
        result += "----------------------------+\n";
        result += String.format("%-14s %-5s %s.000 %n", "Total", totalItem, (totalPrice) / 1000);

        return result;
    }

    private void printHeader(String state) {
        switch (state) {
            case "menu":
                System.out.print(determineHeaderContent("Selamat datang di BinarFud"));
                break;
            case "menuSelected":
                System.out.print(determineHeaderContent("Berapa pesanan anda"));
                break;
            case "confirmation":
                System.out.print(determineHeaderContent("Konfirmasi & Pembayaran"));
                break;
            case "invoice":
                System.out.print(determineHeaderContent("BinarFud"));
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
