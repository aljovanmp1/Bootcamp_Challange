package challenge_4.binarfud.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.Merchant;
import challenge_4.binarfud.repository.ProductRepository;
import challenge_4.binarfud.utlis.DataNotFoundException;

@Service
public class ProductService {
    private @Getter LinkedHashMap<Long, Product> menuList;
    private @Setter Merchant merchant = new Merchant();

    private final ProductRepository menuRepository;
    private final MerchantService merchantService;

    public ProductService(ProductRepository menuRepository, MerchantService merchantService) {
        this.menuRepository = menuRepository;
        this.merchantService = merchantService;
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

    public Product addMenu(String productName, Integer price, Long merchantId) {
        Product newMenu = new Product();
        newMenu.setPrice(price);
        newMenu.setProductName(productName);

        try {
            newMenu.setMerchant(merchantService.getOneMerchant(merchantId));
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return newMenu;
    }

    public void addMenuFromEntity(Product menu) {
        menuRepository.save(menu);
    }

    public Optional<Product> getMenuById(Long id) {
        return Optional.ofNullable(menuList.get(id));
    }


    public Map<Long, Product> getAllMenuByMerchant() {
        List<Product> allMenu = getAllMenuByMerchant(merchant);

        var ind = new Object(){ Long value = 1L; };
        Function<Product, Long> getInd = id -> ind.value++;

        return allMenu.stream()
                .collect(Collectors.toMap(
                        getInd,
                        Function.identity()
                    ));
    }

    public void refetchData() {
        menuList = new LinkedHashMap<>(getAllMenuByMerchant());
    }

    public void deleteAllMenu() {
        menuRepository.deleteAll();
    }

    public void restartSeq() {
        menuRepository.resetSeq();
    }

    public List<Product> getAllMenuByMerchant(Merchant merchant) {
        return menuRepository.findByMerchantOrderByIdAsc(merchant);
    }

    public void setMenuPrice(Product menu, Integer price) {
        menu.setPrice(price);
        menuRepository.save(menu);
    }

    public void setMenuName(Product menu, String name) {
        menu.setProductName(name);
        menuRepository.save(menu);
    }

    public void deleteProduct(Product product) {
        menuRepository.delete(product);
    }

}
