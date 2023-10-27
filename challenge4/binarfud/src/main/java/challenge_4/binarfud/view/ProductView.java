package challenge_4.binarfud.view;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.OrderDetail;

import java.util.List;
import java.util.Map;


public class ProductView extends View{
    public static void showMenuData(List<Product> menuData){
        int ind = 1;
        System.out.printf("\n%s %-20s %s\n", "no." , "menu", "harga");
        for(Product menu : menuData){
            System.out.printf("%-3d %-20s %s\n", ind++, menu.getProductName(), menu.getPrice());
        }
    }
    public static void printMenu(Map<Long, Product> menuList){
        printHeader("menu");
        int ind = 1;
        System.out.println("Silahkan pilih makanan : ");
        for (Map.Entry<Long, Product> entry : menuList.entrySet()) {
            Product menu = entry.getValue();
            System.out.printf("%d. %-13s| %d.000%n", ind, menu.getProductName(), menu.getPrice() / 1000);
            ind++;
        }
        System.out.println("\n99. Pesan dan Bayar");
        System.out.println("98. Kembali ke menu utama");
        System.out.println("0. Keluar Aplikasi");
        System.out.println();
    }

    public static void printSubmitNote(){
        System.out.print("Masukkan Keterangan Tambahan => ");
    }

    
    public static void printSelectedMenu(Product selectedMenu) {
        printHeader("menuSelected");
        System.out.printf("%-13s| %d.000%n", selectedMenu.getProductName(), selectedMenu.getPrice() / 1000);
        System.out.println("(input 0 untuk kembali)");
        System.out.println();
    }

    private static void printHeader(String state) {
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

    public static void printConfirmation(Map<Long, OrderDetail> orderQty, Map<Long, Product> menuList, Integer totalItem, Integer totalPrice){
        printHeader("confirmation");
        System.out.print(formatOrderConfirmation(orderQty, menuList, totalItem, totalPrice));

        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke pilih menu");
        System.out.println("0. Keluar aplikasi");
        System.out.println();
        
    }

    public static String formatOrderConfirmation(Map<Long, OrderDetail> orderQty, Map<Long, Product> menuList, Integer totalItem, Integer totalPrice) {
        String result = "";
    
        StringBuilder bld = new StringBuilder();
        int longestOrder = 0;
    
        for (Map.Entry<Long, OrderDetail> entry : orderQty.entrySet()) {
            
            Long key = entry.getKey();
            OrderDetail order = entry.getValue();
            Integer qty = order.getQuantity();
            String note = order.getNote();
            String menuName = menuList.get(key).getProductName();
    
            Integer price = order.getTotalPrice() / 1000;
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
}
