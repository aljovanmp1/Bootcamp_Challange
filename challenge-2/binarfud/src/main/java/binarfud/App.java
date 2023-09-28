package binarfud;

import binarfud.service.AppService;

public class App {
    public static void main(String[] args) {
        AppService app = new AppService();
        app.start();
    }
}
