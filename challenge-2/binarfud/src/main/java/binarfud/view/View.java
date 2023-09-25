package binarfud.view;

import java.util.LinkedHashMap;
import lombok.NoArgsConstructor;

import binarfud.model.Menu;

@NoArgsConstructor
public class View {

    public void printMenu(LinkedHashMap<Integer, Menu> menuList){
        printHeader("menu");
        int ind = 1;
        System.out.println("Silahkan pilih makanan : ");
        for (Integer key : menuList.keySet()) {
            Menu menu = menuList.get(key);

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

    public void printConfirmation(LinkedHashMap<Integer, Integer> orderQty, LinkedHashMap<Integer, Menu> menuList){
        printHeader("confirmation");
        System.out.print(formatOrderConfirmation(orderQty, menuList));

        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.println();
    }

    public String formatOrderConfirmation(LinkedHashMap<Integer, Integer> orderQty, LinkedHashMap<Integer, Menu> menuList) {
        String result = "";
        int totalItem = 0;
        int totalPrice = 0;

        for (Integer key : orderQty.keySet()) {
            result += String.format("%-14s %-5s %s.000 %n", menuList.get(key).getName(), orderQty.get(key),
                    (orderQty.get(key) * menuList.get(key).getPrice()) / 1000);
            totalItem += orderQty.get(key);
            totalPrice += orderQty.get(key) * menuList.get(key).getPrice();
        }

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
        }
    }

    public String determineHeaderContent(String sentence) {
        String result = "";
        result += '\n';
        result += "========================\n";
        result += sentence + '\n';
        result += "========================\n\n";
        return result;
    }

    public void printError(String state){
        String sentence = "";
        String footer = "";
        switch(state) {
            case "wrongInput":
                sentence+= "Mohon masukkan input\n";
                sentence+= "pilihan anda";
                footer+= "(Y) untuk lanjut\n";
                footer+= "(n) untuk keluar";
                break;
            case "emptyOrder":
                sentence+= "Minimal 1 jumlah\n";
                sentence+= "pesanan!";
                break;
        }

        String result = "";
        result += '\n';
        result += "========================\n";
        result += sentence + '\n';
        result += "========================\n";
        result += footer + "\n";
        
        System.out.print(result);
    }
}
