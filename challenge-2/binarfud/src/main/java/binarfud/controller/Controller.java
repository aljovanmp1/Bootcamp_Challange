package binarfud.controller;

import java.util.LinkedHashMap;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

import binarfud.model.Menu;
import binarfud.view.View;
import binarfud.utlis.WrongInputException;

public class Controller {
    String state;
    boolean orderIsFinished = false;
    Menu selectedMenu;
    String savePath = "./struk.txt";

    Scanner input = new Scanner(System.in);
    private LinkedHashMap<Integer, Menu> menuList;
    private LinkedHashMap<Integer, Integer> orderQty;
    private View view;

    public Controller(View view, LinkedHashMap<Integer, Integer> orderQty, LinkedHashMap<Integer, Menu> menuList) {
        this.view = view;
        this.orderQty = orderQty;
        this.menuList = menuList;
        this.state = "menu";
    }

    public void receivedOrder() {
        while (!this.orderIsFinished) {
            try {
                switch (this.state) {
                    case "menu":
                        pickMenu();
                        break;
                    case "menuSelected":
                        pickQuantity();
                        break;
                    case "confirmation":
                        pickConfirmation();
                        break;
                    case "invoice":
                        System.out.print(getInvoice());
                        saveInvoiceToFile();
                        break;
                    case "exit":
                        this.orderIsFinished = true;
                        break;
                }

            } catch (IOException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            } catch (WrongInputException e) {
                loop: while (true) {
                    System.out.print("=> ");
                    String inp = input.nextLine();
                    switch (inp) {
                        case "n":
                            this.state = "exit";
                            break loop;
                        case "Y":
                            break loop;
                        default:
                            view.printError("wrongInput");
                            continue;
                    }
                }
            }
        }
    }

    private void pickMenu() throws WrongInputException {
        view.printMenu(menuList);

        while (true) {
            System.out.print("=> ");
            String inp = input.nextLine();

            inp = handleIntInput(inp);

            String inpBuf = inp;
            int menuListLength = menuList.keySet().toArray().length;

            if (inp.equals("100"))
                inp = "-1";
            if (inp != "-1" && Integer.parseInt(inp) <= menuListLength && !inp.equals("0"))
                inp = "100";

            switch (inp) {
                case "0":
                    this.state = "exit";
                    return;
                case "100":
                    this.state = "menuSelected";
                    this.selectedMenu = menuList.get(Integer.parseInt(inpBuf));
                    return;
                case "99":
                    this.state = "confirmation";
                    return;
                default:
                    view.printError("wrongInput");
                    throw new WrongInputException("Wrong Input");
            }

        }
    }

    private void pickQuantity() throws WrongInputException  {
        view.printSelectedMenu(this.selectedMenu);
        while (true) {
            System.out.print("qty => ");
            String inp = input.nextLine();

            inp = handleIntInput(inp);

            switch (inp) {
                case "0":
                    this.state = "menu";
                    return;
                case "-1":
                    view.printError("wrongInput");
                    throw new WrongInputException("Wrong Input");
                default:
                    this.orderQty.put(selectedMenu.getId(), Integer.parseInt(inp));
                    this.state = "menu";
                    return;
            }
        }
    }

    private void pickConfirmation() throws WrongInputException {
        view.printConfirmation(orderQty, menuList);

        while (true) {
            System.out.print("=> ");
            String inp = input.nextLine();

            switch (inp) {
                case "1":
                    if (orderQty.keySet().toArray().length < 1) {
                        view.printError("emptyOrder");
                        this.state = "menu";
                        return;
                    }
                    this.state = "invoice";
                    return;
                case "2":
                    this.state = "menu";
                    return;
                case "0":
                    this.state = "exit";
                    return;
                default:
                    view.printError("wrongInput");
                    throw new WrongInputException("Wrong Input");
            }
        }
    }

    private String getInvoice() {
        String result = "";
        result += view.determineHeaderContent("BinarFud");

        result += "Terima kasih sudah memesan\n";
        result += "di BinarFud\n\n";

        result += "Dibawah ini adalah pesanan anda\n";

        result += view.formatOrderConfirmation(orderQty, menuList);
        result += "\nPembayaran : BinarCash\n";

        result += "\n========================\n";
        result += "Simpan struk ini sebagai\n";
        result += "bukti pembayaran\n";
        result += "========================\n\n";

        this.state = "exit";
        return result;

    }

    private void saveInvoiceToFile() throws IOException {
        boolean isAppend = false;
        FileWriter writer = new FileWriter(this.savePath, isAppend);
        PrintWriter printLine = new PrintWriter(writer);

        printLine.print(getInvoice());
        printLine.close();
    }

    private String handleIntInput(String inp) {
        try {
            int num = Integer.parseInt(inp);
            if (num < 0) {
                return "-1";
            }
            return inp;
        } catch (Exception e) {
            return "-1";
        }
    }

}
