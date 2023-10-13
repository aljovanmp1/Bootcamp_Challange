package binarfud.challenge3.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import binarfud.challenge3.utlis.WrongInputException;
import binarfud.challenge3.service.AppService;
import binarfud.challenge3.service.OrderService;
import binarfud.challenge3.utlis.Constants;

@SpringBootTest
class ControllerTest {

    // @Autowired
    // private AppService appService;

    @Autowired
    private Controller controller;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("Positive Test - pick menu")
    void pickMenuStateTest() {
        String[] menuOptions = { "0", "99", "1", "2", "3", "4", "5" };
        String[] states = { "exit", "confirmation", "menuSelected", "menuSelected", "menuSelected", "menuSelected",
                "menuSelected" };
        String[] stateResults = new String[7];
        try {
            int ind = 0;
            for (String option : menuOptions) {
                controller.pickMenu(option);
                stateResults[ind] = controller.getState();
                ind++;
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        int ind = 0;
        for (String state : states) {
            Assertions.assertEquals(state, stateResults[ind]);
            ind++;
        }
    }

    @Test
    @DisplayName("Negative Test - Wrong Input")
    void pickFalseMenuTest(){
        String[] menuOptions = { "-2", "6", "a"};
        
        for (String option : menuOptions){
            WrongInputException e = Assertions.assertThrows(WrongInputException.class, ()->controller.pickMenu(option));
            Assertions.assertEquals(Constants.ERR_WRONGINPUT, e.getMessage());
        }
    }

    @Test
    @DisplayName("Positive Test - pick qty and options")
    void pickQuantityAndMenu(){
        String[] options = {"0", "1"};
        String[] stateResults = {"menu", "note"};

        String[] actualStateResults = new String[2];
        try {
            int ind = 0;
            for (String option : options) {
                controller.pickMenu("1");
                controller.pickQuantity(option);
                actualStateResults[ind] = controller.getState();
                ind++;
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        int ind = 0;
        for (String state : stateResults) {
            Assertions.assertEquals(state, actualStateResults[ind]);
            ind++;
        }
    }

    @Test
    @DisplayName("Negative Test - Wrong Input")
    void pickFalseQuantityTest(){
        String[] menuOptions = { "-2", "a"};
        
        for (String option : menuOptions){
            WrongInputException e = Assertions.assertThrows(WrongInputException.class, ()->controller.pickQuantity(option));
            Assertions.assertEquals(Constants.ERR_WRONGINPUT, e.getMessage());
        }
    }

    @Test
    @DisplayName("Positive Test - submit note")
    void submitNote() {
        try{
            controller.pickMenu("1");
            controller.pickQuantity("2");
        } catch (Exception e){
            System.out.println("submitNote: " + e.getMessage());
        }
        controller.submitNote("Pedes");
        Assertions.assertEquals("menu", controller.getState());
    }

    @Test
    @DisplayName("Positive Test - pick confirmation")
    void pickConfirmationTest(){
        String[] options = {"0", "1", "2"};
        String[] stateResults = {"exit", "invoice", "menu"};

        String[] actualStateResults = new String[3];

        try {
            controller.pickMenu("1");
            controller.pickQuantity("2");
            controller.submitNote("Pedes");

            int ind = 0;
            for (String option : options) {
                controller.pickConfirmation(option);
                actualStateResults[ind] = controller.getState();
                ind++;
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        int ind = 0;
        for (String state : stateResults) {
            Assertions.assertEquals(state, actualStateResults[ind]);
            ind++;
        }
    }

    @Test
    @DisplayName("Negative Test - Wrong Input Confirmation")
    void pickFalseConfirmationTest(){
        String[] menuOptions = { "-2", "a"};
        
        for (String option : menuOptions){
            WrongInputException e = Assertions.assertThrows(WrongInputException.class, ()->controller.pickConfirmation(option));
            Assertions.assertEquals(Constants.ERR_WRONGINPUT, e.getMessage());
        }
    }

    @Test
    @DisplayName("Negative Test - Empty Order")
    void pickEmptyOrderConfirmationTest(){
        try{
            orderService.clearOrder();
            controller.pickConfirmation("1");
        } catch(Exception e){
            System.out.println("confirmation err:" + e.getMessage());
        }
        Assertions.assertEquals("menu", controller.getState());
    }
}
