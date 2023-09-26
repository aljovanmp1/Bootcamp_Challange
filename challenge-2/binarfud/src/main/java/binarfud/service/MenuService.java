package binarfud.service;

import java.util.LinkedHashMap;
import lombok.Getter;

import binarfud.model.Menu;

public class MenuService {
    private @Getter LinkedHashMap<Integer, Menu>  menuList = new LinkedHashMap<Integer, Menu>();

    public MenuService(){
        menuList.put(1, new Menu(1, "Nasi Goreng", 15000));
        menuList.put(2, new Menu(2, "Mie Goreng", 13000));
        menuList.put(3, new Menu(3, "Nasi + Ayam", 18000));
        menuList.put(4, new Menu(4, "Es Teh Manis", 3000));
        menuList.put(5, new Menu(5, "Es Jeruk", 5000));
    }
}
