package binarfud;

import java.util.LinkedHashMap;

import binarfud.controller.Controller;
import binarfud.model.Menu;
import binarfud.view.View;

public class App {
    public static void main(String[] args) {
        View view = new View();
        LinkedHashMap<Integer, Menu> menuList = new LinkedHashMap<Integer, Menu>() {
            {
                put(1, new Menu(1, "Nasi Goreng", 15000));
                put(2, new Menu(2, "Mie Goreng", 13000));
                put(3, new Menu(3, "Nasi + Ayam", 18000));
                put(4, new Menu(4, "Es Teh Manis", 3000));
                put(5, new Menu(5, "Es Jeruk", 5000));
            }
        };

        LinkedHashMap<Integer, Integer> orderQty =  new LinkedHashMap<Integer, Integer>();
        Controller controller = new Controller(view, orderQty, menuList);

        controller.receivedOrder();

    }
}
