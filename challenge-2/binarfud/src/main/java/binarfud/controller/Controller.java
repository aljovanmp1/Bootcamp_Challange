package binarfud.controller;

import java.util.LinkedHashMap;
import java.io.IOException;
import java.util.Scanner;
import lombok.NoArgsConstructor;

import binarfud.model.Menu;
import binarfud.view.View;
import binarfud.utlis.WrongInputException;
import binarfud.utlis.Constants;
import binarfud.service.StruckService;
import binarfud.service.MenuService;
import binarfud.service.OrderService;

@NoArgsConstructor
public class Controller {
    String state;
    boolean orderIsFinished = false;
    Menu selectedMenu;
    
    Scanner input = new Scanner(System.in);
    private LinkedHashMap<Integer, Menu> menuList;
    private LinkedHashMap<Integer, Integer> orderQty;
    private View view;

    private StruckService struckService;

    public void menu(){
        this.view = new View();
        
        this.struckService = new StruckService();
        OrderService orderService = new OrderService();
        MenuService menuService = new MenuService();

        this.orderQty = orderService.getOrderQty();
        this.menuList = menuService.getMenuList();
        this.state = "menu";

        selectMenu();
    }

    public void selectMenu() {
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
                        String invoice = struckService.getInvoice(orderQty, menuList);
                        System.out.print(invoice);
                        struckService.saveInvoiceToFile(invoice);
                        this.state = "exit";
                        break;
                    case "exit":
                        this.orderIsFinished = true;
                        break;
                    default:
                        this.orderIsFinished = true;
                }

            } catch (IOException e) {
                System.out.println("Something went wrong: " + e.getMessage());

            } catch (WrongInputException e) {
                boolean isStillWrong = true;
                while (isStillWrong) {
                    System.out.print("=> ");
                    String inp = input.nextLine();
                    switch (inp) {
                        case "n":
                            this.state = "exit";
                            isStillWrong = false;
                            break;
                        case "Y":
                            isStillWrong = false;
                            break;
                        default:
                            view.printError(Constants.WRONGINPUT);
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
                    view.printError(Constants.WRONGINPUT);
                    throw new WrongInputException(Constants.ERR_WRONGINPUT);
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
                    view.printError(Constants.WRONGINPUT);
                    throw new WrongInputException(Constants.ERR_WRONGINPUT);
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
                    view.printError(Constants.WRONGINPUT);
                    throw new WrongInputException(Constants.ERR_WRONGINPUT);
            }
        }
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
