package binarfud.service;

import binarfud.controller.Controller;

public class AppService {
    public void start() {
        Controller controller = new Controller();
        controller.menu();
    }
}
