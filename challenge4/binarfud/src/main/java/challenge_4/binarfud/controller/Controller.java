package challenge_4.binarfud.controller;

import java.util.Scanner;

public class Controller {
    Scanner input = new Scanner(System.in);

    protected String receiveInputHandler(String flag) {
        System.out.print(flag);
        return input.nextLine();
    }

    protected String handleIntInput(String inp) {
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
