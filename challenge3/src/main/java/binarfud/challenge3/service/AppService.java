package binarfud.challenge3.service;

import org.springframework.stereotype.Service;

import binarfud.challenge3.controller.Controller;
import lombok.Getter;

@Service
public class AppService {
    private final @Getter Controller controller;
    private final MenuService menuService;
    private final MerchantService merchantService;

    public AppService(Controller controller, MenuService menuService, MerchantService merchantService) {
        this.controller = controller;
        this.menuService = menuService;
        this.merchantService = merchantService;    
    }

    
    public void start() {
        controller.menu();
    }

    public void initiateData(){
        merchantService.initData();
        menuService.initData();
    }
}
