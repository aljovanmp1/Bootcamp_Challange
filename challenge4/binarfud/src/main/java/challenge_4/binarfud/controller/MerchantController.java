package challenge_4.binarfud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.Merchant;
import challenge_4.binarfud.service.ProductService;
import challenge_4.binarfud.service.MerchantService;
import challenge_4.binarfud.utlis.WrongInputException;
import challenge_4.binarfud.view.ProductView;
import challenge_4.binarfud.view.MerchantView;
import challenge_4.binarfud.view.View;
import challenge_4.binarfud.utlis.Constants;

@Component
public class MerchantController extends Controller {
    boolean exit = false;
    String state = "merchantHome";
    Merchant selectedMerchant;

    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    MerchantService merchantService;

    @Autowired
    ProductService menuService;

    public boolean selectMerchantMenu() {
        String inp;
        state = "merchantHome";
        exit = false;
        while (!exit) {
            try {
                switch (this.state) {
                    case "merchantHome":
                        MerchantView.merchantOptions();
                        inp = receiveInputHandler("=> ");
                        pickMerchantHome(inp);
                        break;
                    case "addMerchant":
                        addMerchant();
                        break;
                    case "editMerchant":
                        pickMerchantAvailibility();
                        break;
                    case "viewOpenMerchant":
                        retrieveOpenMerchant();
                        break;
                    case "selectMerchantInProduct":
                        selectMerchantInProduct();
                        break;
                    case "pickAddOrEditProduct":
                        pickAddOrEditProduct();
                        break;
                    case "editProduct":
                        pickEditProduct();
                        break;
                    case "addProduct":
                        addProduct();
                        break;
                    case "deleteProduct":
                        deleteProduct();
                        break;
                    case "exit":
                        return true;
                    case "home":
                        exit = true;
                        break;
                    default:
                        state = "merchantHome";
                }
            }

            catch (WrongInputException e) {
                boolean isStillWrong = true;
                View.printError(Constants.WRONGINPUT);
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
            }

            catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        return false;
    }

    private void pickMerchantHome(String inp) throws WrongInputException {
        switch (inp) {
            case "1" -> state = "addMerchant";
            case "2" -> state = "editMerchant";
            case "3" -> state = "viewOpenMerchant";
            case "4" -> state = "selectMerchantInProduct";
            case "99" -> state = "home";
            case "0" -> state = "exit";
            default -> throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }

    }

    private void addMerchant() {
        System.out.print("Nama merchant: ");
        String merchantName = receiveInputHandler("");
        System.out.print("Lokasi merchant: ");
        String merchantLoc = receiveInputHandler("");

        Merchant merchant = new Merchant();
        merchant.setMerchantName(merchantName);
        merchant.setMerchantLocation(merchantLoc);
        merchant.setOpen(false);

        try {
            merchantService.addMerchant(merchant);
        } catch (Exception e) {
            System.out.println("error in add merchant" + e.getMessage());
        }

        state = "merchantHome";
    }

    private void pickMerchantAvailibility() throws WrongInputException {
        List<Merchant> merchants = merchantService.getAllMerchants();
        MerchantView.showMerchantAvailability(merchants);
        System.out.println("\nPilih no merchant atau 0 untuk kembali");

        String inp = receiveInputHandler("=> ");
        inp = handleIntInput(inp);

        if (!inp.equals("-1") && Integer.parseInt(inp) > merchants.size()) {
            inp = "-1";
        }

        switch (inp) {
            case "-1" -> {
                View.printError(Constants.WRONGINPUT);
                throw new WrongInputException(Constants.ERR_WRONGINPUT);
            }
            case "0" -> {
                state = "merchantHome";
            }

            default -> {
                Integer selectedInd = Integer.parseInt(inp);
                merchantService.setMerchantAvailability(merchants.get(selectedInd - 1));
            }
        }

    }

    private void retrieveOpenMerchant() {
        boolean isExitPagination = false;
        int page = 0;
        while (!isExitPagination) {
            Page<Merchant> merchantPagination = merchantService.getAllOpenMerchants(page);
            List<Merchant> merchants = merchantPagination.getContent();

            System.out.println("merchant: "+ merchants);

            Integer totalPage = merchantPagination.getTotalPages();
            MerchantView.showMerchantsWithPagination(merchants, 5, page+1);
            MerchantView.showMerchantPage(merchantPagination.getNumber() + 1, totalPage);
            
            String pageInp = receiveInputHandler("=> ");
            Integer pageInpInt = Integer.parseInt(pageInp);

            if (pageInp.equals("0")) isExitPagination = true;
            else if (pageInpInt > totalPage)
                page = totalPage - 1;
            else 
                page = pageInpInt - 1;
        }
        state = "merchantHome";
    }

    private void pickEditProduct() throws WrongInputException {

        List<Product> menuData = menuService.getAllMenuByMerchant(selectedMerchant);

        ProductView.showMenuData(menuData);

        if (menuData.size() == 0) {
            System.out.print("\nBelum ada menu di merchant ini.\nMasukkan apapun untuk kembali");
            receiveInputHandler("");
            state = "pickAddOrEditProduct";
            return;
        }

        System.out.print("\nSilahkan pilih nomor menu: ");
        String productInd = receiveInputHandler("");

        productInd = handleIntInput(productInd);
        Integer productIndInt = Integer.parseInt(productInd);

        if (productIndInt < 1 || productIndInt > menuData.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        Product selectedMenu = menuData.get(productIndInt - 1);

        MerchantView.editProductOptions();

        String inp = receiveInputHandler("\n=> ");

        switch (inp) {
            case "1" -> {
                String productName = receiveInputHandler("\nMasukkan nama baru menu: ");
                menuService.setMenuName(selectedMenu, productName);
                state = "merchantHome";
            }
            case "2" -> {
                String productPrice = receiveInputHandler("\nMasukkan harga baru menu: ");
                productPrice = handleIntInput(productPrice);
                Integer priceInt = Integer.parseInt(productPrice);

                if (priceInt < 0)
                    throw new WrongInputException(Constants.ERR_WRONGINPUT);

                menuService.setMenuPrice(selectedMenu, priceInt);
                state = "merchantHome";
            }

            case "99" -> state = "home";
            case "0" -> state = "exit";
            default -> throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }
    }

    private void selectMerchantInProduct() throws WrongInputException {
        List<Merchant> merchants = merchantService.getAllMerchants();
        MerchantView.showMerchants(merchants);

        System.out.print("\nSilahkan pilih nomor merchant: ");
        String merchantInd = receiveInputHandler("");
        merchantInd = handleIntInput(merchantInd);
        Integer merchantIndInt = Integer.parseInt(merchantInd);

        if (merchantIndInt < 1 || merchantIndInt > merchants.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        selectedMerchant = merchants.get(merchantIndInt - 1);

        state = "pickAddOrEditProduct";

    }

    private void pickAddOrEditProduct() throws WrongInputException {
        MerchantView.addOrEditProductOptions();

        String inp = receiveInputHandler("\n=> ");

        switch (inp) {
            case "1" -> state = "addProduct";
            case "2" -> state = "editProduct";
            case "3" -> state = "deleteProduct";

            case "99" -> state = "home";
            case "0" -> state = "exit";
            default -> throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }
    }

    private void addProduct() throws WrongInputException {
        System.out.print("Nama menu baru: ");
        String newProductName = receiveInputHandler("");

        System.out.print("Harga menu baru: ");
        String priceInp = receiveInputHandler("");
        priceInp = handleIntInput(priceInp);
        Integer newPrice = Integer.parseInt(priceInp);

        if (newPrice < 0)
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        Product newProduct = new Product();
        newProduct.setProductName(newProductName);
        newProduct.setPrice(newPrice);
        newProduct.setMerchant(selectedMerchant);
        menuService.addMenuFromEntity(newProduct);

        state = "merchantHome";
    }

    private void deleteProduct() throws WrongInputException {
        List<Product> menuData = menuService.getAllMenuByMerchant(selectedMerchant);
        ProductView.showMenuData(menuData);

        String inpInd = receiveInputHandler("\nSilahkan pilih produk yg ingin dihapus atau 0 untuk kembali: ");
        inpInd = handleIntInput(inpInd);
        Integer inpIndInt = Integer.parseInt(inpInd);

        if (inpIndInt == 0) {
            state = "pickAddOrEditProduct";
            return;
        }

        else if (inpIndInt < 1 || inpIndInt > menuData.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);
        
        menuService.deleteProduct(menuData.get(inpIndInt-1));
        state = "merchantHome";
    }
}
