package challange_5.binarfud.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

import challange_5.binarfud.model.Product;
import challange_5.binarfud.model.Merchant;
import challange_5.binarfud.repository.ProductRepository;
import challange_5.binarfud.utlis.DataNotFoundException;

@Service
public class ProductService {
    private @Getter LinkedHashMap<Long, Product> menuList;
    private @Setter Merchant merchant = new Merchant();

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository, MerchantService merchantService) {
        this.productRepository = productRepository;
    }

    public Product addOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }


    public void deleteAllMenu() {
        productRepository.deleteAll();
    }

    public Page<Product> getAllProductByMerchant(UUID merchantId, int page, int pageSize) {
        return productRepository.findByMerchantIdAndIsDeleted(merchantId, false, PageRequest.of(page,pageSize));
    }

    public int getCountByMerchantId(UUID merchantId){
        return productRepository.countByMerchantId(merchantId);
    }

    public void setMenuPrice(Product menu, Integer price) {
        menu.setPrice(price);
        productRepository.save(menu);
    }

    public void setMenuName(Product menu, String name) {
        menu.setProductName(name);
        productRepository.save(menu);
    }

    public Product deleteProduct(Product product) {
        product.setDeleted(true);
        return productRepository.save(product);
    }

}
