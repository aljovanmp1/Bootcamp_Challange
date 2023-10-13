package binarfud.challenge3.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.Getter;

import binarfud.challenge3.model.Menu;
import binarfud.challenge3.repository.MenuRepository;
import binarfud.challenge3.utlis.DataNotFoundException;

@Service
public class MenuService {
    private @Getter LinkedHashMap<Long, Menu> menuList;

    private final MenuRepository menuRepository;
    private final MerchantService merchantService;

    public MenuService(MenuRepository menuRepository, MerchantService merchantService) {
        this.menuRepository = menuRepository;
        this.merchantService = merchantService;
        menuList = new LinkedHashMap<>(getAllMenu());
    }

    public void initData() {
        if (menuRepository.count() == 0) {
            menuRepository.save(addMenu("Nasi Goreng", 15000, 1L));
            menuRepository.save(addMenu("Mie Goreng", 13000, 1L));
            menuRepository.save(addMenu("Nasi + Ayam", 18000, 1L));
            menuRepository.save(addMenu("Es The Manis", 3000, 1L));
            menuRepository.save(addMenu("Es Jeruk", 5000, 1L));
        }
        
    }

    public Menu addMenu(String productName, Integer price, Long merchantId) {
        Menu newMenu = new Menu();
        newMenu.setPrice(price);
        newMenu.setProductName(productName);

        try {
            newMenu.setMerchant(merchantService.getOneMerchant(merchantId));
            System.out.println("merchant:" + newMenu.getMerchant());
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return newMenu;
    }

    public Optional<Menu> getMenuById(Long id) {
        return Optional.ofNullable(menuList.get(id));
    }

    public Map<Long, Menu> getAllMenu() {
        List<Menu> allMenu = menuRepository.findAll();

        return allMenu.stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
    }

    public void refetchData(){
        menuList = new LinkedHashMap<>(getAllMenu());
    }

    public void deleteAllMenu(){
        menuRepository.deleteAll();
    }

    public void restartSeq(){
        menuRepository.resetSeq();
    }

}
