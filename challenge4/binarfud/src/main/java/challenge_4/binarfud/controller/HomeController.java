package challenge_4.binarfud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import challenge_4.binarfud.view.HomeView;

@Component
public class HomeController extends Controller {
    boolean stopRun = false;
    String state = "home";

    @Autowired
    MerchantController merchantController;
    @Autowired
    UserController userController;
    @Autowired
    ProductController menuController;

    public void home() {
        selectMainMenu();
        state = "home";
    }

    public void selectMainMenu() {
        String inp;
        while (!stopRun) {
            switch (this.state) {
                case "home":
                    HomeView.welcomeMessage();
                    HomeView.mainMenuOption();
                    inp = receiveInputHandler("=> ");
                    pickMainMenu(inp);
                    break;
                case "merchant":
                    state = merchantController.selectMerchantMenu() ? "exit" : "home";
                    break;
                case "user":
                    state = userController.selectUserMenu() ? "exit" : "home";
                    break;
                case "order":
                    state = menuController.menu() ? "exit" : "home"; 
                    break;
                case "exit":
                    stopRun = true;
                    break;
                default:
                    stopRun = true;
            }
        }
    }

    public void pickMainMenu(String inp) {
        switch (inp) {
            case "1" -> state = "merchant";
            case "2" -> state = "user";
            case "3" -> state = "order";
            case "0" -> state = "exit";
            default -> state = "home";
        }
    }
}