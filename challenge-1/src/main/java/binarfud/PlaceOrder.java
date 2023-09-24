package binarfud;

import java.util.LinkedHashMap;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class PlaceOrder {
    boolean orderIsFinished = false;

    LinkedHashMap<String, Integer> menu;
    LinkedHashMap<String, Integer> order;
    Scanner input = new Scanner(System.in);
    String state;
    String selectedMenu;
    String savePath = "./struk.txt";

    PlaceOrder() {
        this.state = "menu";
        this.menu = new LinkedHashMap<String, Integer>(5, 0.75f) {
            {
                put("Nasi Goreng", 15000);
                put("Mie Goreng", 13000);
                put("Nasi + Ayam", 18000);
                put("Es Teh Manis", 3000);
                put("Es Jeruk", 5000);
            }
        };

        this.order = new LinkedHashMap<String, Integer>();
        System.out.println();
    }

    void receivedOrder() {
        try {
            while (!this.orderIsFinished) {
                switch (this.state) {
                    case "menu":
                        printMenu();
                        break;
                    case "menuSelected":
                        printSelectedMenu();
                        break;
                    case "confirmation":
                        printConfirmation();
                        break;
                    case "invoice":
                        System.out.print(getInvoice());
                        saveInvoiceToFile();
                        break;
                    case "exit":
                        this.orderIsFinished = true;
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    void printMenu() {
        printHeader();
        int ind = 1;
        System.out.println("Silahkan pilih makanan : ");
        for (String key : menu.keySet()) {
            System.out.printf("%d. %-13s| %d.000%n", ind, key, menu.get(key) / 1000);
            ind++;
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar Aplikasi");
        System.out.println();

        int number = -1;

        boolean dataInputIsCorrect = false;
        while (!dataInputIsCorrect) {
            System.out.print("=> ");
            number = handleIntInput();

            if (number == -1)
                continue;

            else if (number > menu.keySet().toArray().length && number != 99)
                System.out.println("Anda hanya dapat memilih menu yang tersedia");

            else
                dataInputIsCorrect = true;
        }

        switch (number) {
            case 0:
                this.state = "exit";
                return;
            case 99:
                this.state = "confirmation";
                return;
        }

        this.state = "menuSelected";
        this.selectedMenu = menu.keySet().toArray()[number - 1].toString();
    }

    void printSelectedMenu() {
        printHeader();
        System.out.printf("%-13s| %d.000%n", this.selectedMenu, menu.get(this.selectedMenu) / 1000);
        System.out.println("(input 0 untuk kembali)");
        System.out.println();

        int number = -1;
        boolean dataInputIsCorrect = false;

        while (!dataInputIsCorrect) {
            System.out.print("qty => ");
            number = handleIntInput();

            if (number == -1)
                continue;
            else
                dataInputIsCorrect = true;
        }

        if (number == 0) {
            this.state = "menu";
            return;
        }

        this.order.put(this.selectedMenu, number);
        this.state = "menu";
    }

    void printConfirmation() {
        printHeader();
        System.out.print(getOrder());

        System.out.println();
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.println();

        int number = -1;
        boolean dataInputIsCorrect = false;
        while (!dataInputIsCorrect) {
            System.out.print("=> ");
            number = handleIntInput();

            if (number == -1)
                continue;
            else
                dataInputIsCorrect = true;
        }

        switch (number) {
            case 1:
                if (order.keySet().toArray().length < 1) {
                    System.out.println("Silahkan masukkan pesanan terlebih dahulu");
                    this.state = "menu";
                    return;
                }
                this.state = "invoice";
                return;
            case 2:
                this.state = "menu";
                return;
            case 0:
                this.state = "exit";
                return;
        }

    }

    String getInvoice() {
        String result = "";
        result += determineHeaderContent("BinarFud");

        result += "Terima kasih sudah memesan\n";
        result += "di BinarFud\n\n";

        result += "Dibawah ini adalah pesanan anda\n";

        result += getOrder();
        result += "\nPembayaran : BinarCash\n";

        result += "\n========================\n";
        result += "Simpan struk ini sebagai\n";
        result += "bukti pembayaran\n";
        result += "========================\n\n";

        this.state = "exit";
        return result;

    }

    void saveInvoiceToFile() throws IOException {
        boolean isAppend = false;
        FileWriter writer = new FileWriter(this.savePath, isAppend);
        PrintWriter printLine = new PrintWriter(writer);

        printLine.print(getInvoice());
        printLine.close();
    }

    String getOrder() {
        String result = "";
        int totalItem = 0;
        int totalPrice = 0;

        for (String key : order.keySet()) {
            result += String.format("%-14s %-5s %s.000 %n", key, order.get(key),
                    (order.get(key) * menu.get(key)) / 1000);
            totalItem += order.get(key);
            totalPrice += order.get(key) * menu.get(key);
        }

        result += "----------------------------+\n";
        result += String.format("%-14s %-5s %s.000 %n", "Total", totalItem, (totalPrice) / 1000);

        return result;
    }

    void printHeader() {
        switch (this.state) {
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

    String determineHeaderContent(String sentence) {
        String result = "";
        result += '\n';
        result += "========================\n";
        result += sentence + '\n';
        result += "========================\n\n";
        return result;
    }

    int handleIntInput() {
        try {
            int num = Integer.parseInt(input.nextLine());

            if (num < 0) {
                System.out.println("Tidak bisa memasukkan nilai minus");
                return -1;
            }
            return num;
        } catch (Exception e) {
            System.out.println("Sepertinya anda tidak memasukkan angka");
            return -1;
        }
    }
}
