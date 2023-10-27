package challenge_4.binarfud.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.Merchant;
import challenge_4.binarfud.model.OrderDetail;
import challenge_4.binarfud.model.Order;
import challenge_4.binarfud.service.ProductService;
import challenge_4.binarfud.service.MerchantService;
import challenge_4.binarfud.service.OrderDetailService;
import challenge_4.binarfud.service.OrderService;
import challenge_4.binarfud.service.StruckService;
import challenge_4.binarfud.view.ProductView;
import challenge_4.binarfud.view.MerchantView;
import challenge_4.binarfud.view.View;
import challenge_4.binarfud.utlis.WrongInputException;
import challenge_4.binarfud.utlis.Constants;

@Component
public class ProductController extends Controller {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    boolean exit = false;
    String state = "pickMerchant";
    private LinkedHashMap<Long, Product> menuList;
    private LinkedHashMap<Long, OrderDetail> orderQty;
    Product selectedMenu;
    Long selectedMenuKey;
    Merchant selectedMerchant;

    @Autowired
    ProductService menuService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    StruckService struckService;
    @Autowired
    MerchantService merchantService;

    public boolean menu() {
        String inp;
        exit = false;
        state = "pickMerchant";
        while (!exit) {
            try {
                switch (this.state) {
                    case "pickMerchant":
                        selectMerchant();
                        break;
                    case "menu":
                        menuService.refetchData();
                        orderQty = this.orderDetailService.getOrderQty();
                        menuList = this.menuService.getMenuList();

                        ProductView.printMenu(menuList);
                        inp = receiveInputHandler("=> ");
                        pickMenu(inp);
                        break;
                    case "menuSelected":
                        ProductView.printSelectedMenu(this.selectedMenu);
                        inp = receiveInputHandler("qty => ");
                        pickQuantity(inp);
                        break;
                    case "note":
                        ProductView.printSubmitNote();
                        inp = receiveInputHandler("");
                        submitNote(inp);
                        break;
                    case "confirmation":
                        ProductView.printConfirmation(orderQty, menuList, orderDetailService.getTotalItem(),
                                orderDetailService.getTotalPrice());

                        inp = receiveInputHandler("=> ");
                        pickConfirmation(inp);
                        break;
                    case "invoice":
                        Order orderEntity = new Order();
                        Order newOrder = orderService.saveOrder(orderEntity, "Dest");

                        orderDetailService.saveOrderDetail(newOrder);

                        String invoice = struckService.getInvoice(orderQty, menuList, orderDetailService.getTotalItem(),
                                orderDetailService.getTotalPrice());

                        System.out.print(invoice);
                        struckService.saveInvoiceToFile(invoice);
                        this.state = "home";
                        break;
                    case "exit":
                        orderDetailService.clearOrder();
                        return true;
                    case "home":
                        orderDetailService.clearOrder();
                        exit = true;
                        break;
                    default:
                        state = "menu";
                }

            } catch (IOException e) {
                logger.error(e.getMessage());

            } catch (WrongInputException e) {
                View.printError(Constants.WRONGINPUT);
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
                            View.printError(Constants.WRONGINPUT);
                            continue;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return false;
    }

    public void pickMenu(String inp) throws WrongInputException {
        inp = handleIntInput(inp);
        boolean isExitOrConfirm = inp.equals("0") || inp.equals("99") || inp.equals("98");
        Long longInp = Long.parseLong(inp);

        if (menuList.size() == 0) {
            System.out.println("Tidak ada menu di merchant ini\n");
            state = "home";
        }

        else if (!isExitOrConfirm && menuList.size() >= longInp) {
            this.selectedMenu = menuList.get(longInp);
            this.selectedMenuKey = longInp;
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
            case "98":
                state = "home";
                return;
            default:
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
                throw new WrongInputException(Constants.ERR_WRONGINPUT);
            default:
                this.orderDetailService.addOrder(selectedMenuKey, selectedMenu, Integer.parseInt(inp));
                this.state = "note";
                return;
        }
    }

    public void submitNote(String inp) {
        orderDetailService.addNote(selectedMenuKey, inp);
        this.state = "menu";
    }

    public void pickConfirmation(String inp) throws WrongInputException {
        switch (inp) {
            case "1":
                if (orderQty.keySet().toArray().length < 1) {
                    View.printError("emptyOrder");
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
                throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }

    }

    private void selectMerchant() throws WrongInputException {
        boolean isExitPagination = false;
        int page = 0;
        Page<Merchant> merchantPagination = merchantService.getAllOpenMerchants(page);
        List<Merchant> merchants = merchantPagination.getContent();
        while (!isExitPagination) {
            merchantPagination = merchantService.getAllOpenMerchants(page);
            merchants = merchantPagination.getContent();

            Integer totalPage = merchantPagination.getTotalPages();

            MerchantView.showMerchantsWithPagination(merchants, 5, page + 1);
            MerchantView.showAndPickMerchantPagination(merchantPagination.getNumber() + 1, totalPage);

            String pageInp = receiveInputHandler("=> ");
            Integer pageInpInt = Integer.parseInt(pageInp);

            if (pageInp.equals("0"))
                isExitPagination = true;
            else if (pageInpInt > totalPage)
                page = totalPage;
            else
                page = pageInpInt - 1;
        }

        System.out.print("\nSilahkan pilih nomor merchant: ");
        String merchantInd = receiveInputHandler("");
        merchantInd = handleIntInput(merchantInd);

        Integer currPage = merchantPagination.getNumber()+1;
        Integer merchantIndInt = Integer.parseInt(merchantInd) - ((currPage - 1) * 5);

        System.out.println("ind: " + merchantIndInt + merchants.size());
        if (merchantIndInt < 1 || merchantIndInt > merchants.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        selectedMerchant = merchants.get(merchantIndInt - 1);

        menuService.setMerchant(selectedMerchant);

        state = "menu";
    }
}
