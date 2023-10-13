package binarfud.challenge3.controller;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import lombok.Getter;

import binarfud.challenge3.model.Menu;
import binarfud.challenge3.model.Order;
import binarfud.challenge3.view.View;
import binarfud.challenge3.utlis.WrongInputException;
import binarfud.challenge3.utlis.Constants;
import binarfud.challenge3.service.StruckService;
import binarfud.challenge3.service.MenuService;
import binarfud.challenge3.service.OrderService;

@Component
public class Controller {
    private @Getter String state = "menu";
    boolean orderIsFinished = false;
    Menu selectedMenu;

    Scanner input = new Scanner(System.in);
    private LinkedHashMap<Long, Menu> menuList;
    private LinkedHashMap<Integer, Order> orderQty;
    private View view;

    private StruckService struckService;
    private OrderService orderService;
    private MenuService menuService;

    public Controller(OrderService orderService, MenuService menuService) {
        this.view = new View();

        this.struckService = new StruckService();
        this.orderService = orderService;
        this.menuService = menuService;

        orderQty = this.orderService.getOrderQty();
        menuList = this.menuService.getMenuList();
    }
    
    public void menu() {
        if (menuList.size()==0) {
            menuService.refetchData();
            menuList = this.menuService.getMenuList();
        }
        String inp;
        
        while (!this.orderIsFinished) {
            try {
                switch (this.state) {
                    case "menu":
                        view.printMenu(menuList);
                        inp = receiveInputHandler("=> ");
                        pickMenu(inp);
                        break;
                    case "menuSelected":
                        view.printSelectedMenu(this.selectedMenu);
                        inp = receiveInputHandler("qty => ");
                        pickQuantity(inp);
                        break;
                    case "note":
                        view.printSubmitNote();
                        inp = receiveInputHandler("");
                        submitNote(inp);
                        break;
                    case "confirmation":
                        view.printConfirmation(orderQty, menuList, orderService.getTotalItem(),
                                orderService.getTotalPrice());

                        inp = receiveInputHandler("=> ");
                        pickConfirmation(inp);
                        break;
                    case "invoice":
                        String invoice = struckService.getInvoice(orderQty, menuList, orderService.getTotalItem(),
                                orderService.getTotalPrice());

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
                    inp = input.nextLine();
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

    public void pickMenu(String inp) throws WrongInputException {
        inp = handleIntInput(inp);

        Optional<Menu> selectedMenuOptional = menuService.getMenuById(Long.parseLong(inp));

        boolean isExitOrConfirm = inp.equals("0") || inp.equals("99");

        if (!isExitOrConfirm && selectedMenuOptional.isPresent()) {
            this.selectedMenu = selectedMenuOptional.get();
            inp = "-2";
        }

        else if (!isExitOrConfirm)
            inp = "-1";

        switch (inp) {
            case "0":
                this.state = "exit";
                return;
            case "-2":
                this.state = "menuSelected";
                return;
            case "99":
                this.state = "confirmation";
                return;
            default:
                view.printError(Constants.WRONGINPUT);
                throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }

    }

    public void pickQuantity(String inp) throws WrongInputException {
        inp = handleIntInput(inp);

        switch (inp) {
            case "0":
                this.state = "menu";
                return;
            case "-1":
                view.printError(Constants.WRONGINPUT);
                throw new WrongInputException(Constants.ERR_WRONGINPUT);
            default:
                this.orderService.addOrder(selectedMenu.getId().intValue(), Integer.parseInt(inp));
                this.state = "note";
                return;
        }
    }

    public void submitNote(String inp) {
        this.orderService.addNote(selectedMenu.getId().intValue(), inp);
        this.state = "menu";
        System.out.println("order: " + this.orderService.getOrderQty());
    }

    public void pickConfirmation(String inp) throws WrongInputException {
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

    private String receiveInputHandler(String flag) {
        System.out.print(flag);
        return input.nextLine();
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
