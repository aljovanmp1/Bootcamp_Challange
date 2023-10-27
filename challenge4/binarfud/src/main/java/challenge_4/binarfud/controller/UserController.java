package challenge_4.binarfud.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import challenge_4.binarfud.model.User;
import challenge_4.binarfud.service.MerchantService;
import challenge_4.binarfud.service.UserService;
import challenge_4.binarfud.utlis.Constants;
import challenge_4.binarfud.utlis.WrongInputException;
import challenge_4.binarfud.view.UserView;
import challenge_4.binarfud.view.View;

@Component
public class UserController extends Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    boolean exit = false;
    String state = "userHome";

    @Autowired
    MerchantService merchantService;

    public boolean selectUserMenu() {
        String inp;
        state = "userHome";
        exit = false;
        while (!exit) {
            try {
                switch (this.state) {
                    case "userHome":
                        UserView.userOptions();
                        inp = receiveInputHandler("=> ");
                        pickUserHome(inp);
                        break;
                    case "addUser":
                        addUser();
                        break;
                    case "editUser":
                        editUser();
                        break;
                    case "deleteUser":
                        pickDelUser();
                        break;
                    case "exit":
                        return true;
                    case "home":
                        exit = true;
                        break;
                    default:
                        state = "userHome";
                }
            }

            catch (WrongInputException e) {
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
            }

            catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        return false;
    }

    private void pickUserHome(String inp) throws WrongInputException {
        switch (inp) {
            case "1" -> state = "addUser";
            case "2" -> state = "editUser";
            case "3" -> state = "deleteUser";
            case "99" -> state = "home";
            case "0" -> state = "exit";
            default -> throw new WrongInputException(Constants.ERR_WRONGINPUT);
        }
    }

    private void addUser() {
        System.out.print("username: ");
        String userName = receiveInputHandler("");
        System.out.print("email: ");
        String email = receiveInputHandler("");
        System.out.print("password: ");
        String password = receiveInputHandler("");

        User user = new User();
        user.setUserName(userName);
        user.setEmailAddress(email);
        user.setPassword(password);

        try {
            userService.addUser(user);
        } catch (Exception e) {
            System.out.println("error in add user" + e.getMessage());
        }

        state = "userHome";
    }

    private void editUser() throws WrongInputException {
        List<User> users = userService.getAll();
        UserView.showUsers(users);

        System.out.print("\nSilahkan pilih nomor user: ");
        String inp = receiveInputHandler("");
        inp = handleIntInput(inp);

        Integer ind = Integer.parseInt(inp);
        if (ind < 1 || ind > users.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        UserView.editOptions();
        inp = receiveInputHandler("\n=> ");

        switch (inp) {
            case "1" -> {
                System.out.print("\nMasukkan email baru: ");
                String email = receiveInputHandler("");
                userService.setUserEmail(users.get(ind - 1), email);
                state = "userHome";
            }

            case "2" -> {
                System.out.print("\nMasukkan password baru: ");
                String pwd = receiveInputHandler("");
                userService.setUserPassword(users.get(ind - 1), pwd);
                state = "userHome";
            }

            case "99" -> state = "home";
            case "0" -> state = "exit";
            default -> state = "userHome";
        }
    }

    private void pickDelUser() throws WrongInputException {
        List<User> users = userService.getAll();
        UserView.showUsers(users);

        System.out.print("\nSilahkan pilih nomor user yg ingin dihapus: ");

        String inp = receiveInputHandler("");
        inp = handleIntInput(inp);

        Integer ind = Integer.parseInt(inp);
        if (ind < 1 || ind > users.size())
            throw new WrongInputException(Constants.ERR_WRONGINPUT);

        userService.deleteUser(users.get(ind - 1));
        state = "userHome";
    }

}
