package challenge6.binarfud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import challenge6.binarfud.dto.product.request.AddProductDto;
import challenge6.binarfud.dto.product.request.GetProdutcByMerchantDto;
import challenge6.binarfud.dto.product.request.PutProductDto;
import challenge6.binarfud.dto.product.response.ProductRespDto;
import challenge6.binarfud.model.Merchant;
import challenge6.binarfud.model.Product;
import challenge6.binarfud.model.User;
import challenge6.binarfud.service.MerchantService;
import challenge6.binarfud.service.ProductService;
import challenge6.binarfud.service.UserService;
import challenge6.binarfud.utlis.DataNotFoundException;
import challenge6.binarfud.utlis.Utils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MerchantService merchantService;

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapperSkipNull;

    @Autowired
    UserService userService;

    private static final String MERCHANT_NOT_FOUND = "Merchant not found";
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddProductDto dataDto,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            Product newProduct = new Product();
            newProduct.setPrice(dataDto.getPrice());
            newProduct.setProductName(dataDto.getProductName());
            newProduct.setMerchant(merchantExistingOpt.get());
            newProduct.setDeleted(false);

            newProduct = productService.addOrUpdateProduct(newProduct);

            ProductRespDto postRespDto = modelMapper.map(newProduct, ProductRespDto.class);

            data.put("products", postRespDto);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }

    @GetMapping()
    @Secured({ "BUYER", "SELLER" })
    public ResponseEntity<Map<String, Object>> getByMerchant(@Valid GetProdutcByMerchantDto params) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            Optional<Merchant> merchantOptional = merchantService.getOneMerchant(params.getMerchantId());

            if (!merchantOptional.isPresent())
                throw new DataNotFoundException(MERCHANT_NOT_FOUND);

            int selectedPage = Utils.handlePage(
                    productService.getCountByMerchantId(params.getMerchantId()),
                    params.getDataPerPage(),
                    params.getSelectedPage());

            Page<Product> products = productService.getAllProductByMerchant(
                    merchantOptional.get().getId(),
                    selectedPage - 1,
                    params.getDataPerPage());

            List<ProductRespDto> respDto = products.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductRespDto.class))
                    .collect(Collectors.toList());

            data.put("products", respDto);
            Utils.addPageResponse(products, response);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> put(@RequestParam UUID id, @RequestBody PutProductDto product,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            Optional<Product> productOptional = productService.getProductById(id);

            Product existingProduct = new Product();
            modelMapper.map(product, existingProduct);

            if (productOptional.isPresent())
                existingProduct.setId(productOptional.get().getId());

            data.put("product", modelMapper.map(
                    productService.addOrUpdateProduct(existingProduct),
                    ProductRespDto.class));

            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PatchMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> patch(@RequestParam UUID id,
            @RequestBody Product product, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            Optional<Product> selectedProduct = productService.getProductById(id);
            if (!selectedProduct.isPresent())
                throw new DataNotFoundException(PRODUCT_NOT_FOUND);

            if (selectedProduct.get().getMerchant().getId() != merchantExistingOpt.get().getId())
                throw new DataNotFoundException("Mechant dont have this product");

            Product updatedProduct = selectedProduct.get();
            modelMapperSkipNull.map(product, updatedProduct);

            ProductRespDto respDto = modelMapper.map(productService.addOrUpdateProduct(updatedProduct),
                    ProductRespDto.class);

            data.put("products", respDto);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam UUID id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            Optional<Merchant> merchantExistingOpt = Optional.ofNullable(user.getMerchant());
            if (!merchantExistingOpt.isPresent())
                throw new DataNotFoundException("User dont have a merchant");

            Optional<Product> selectedProduct = productService.getProductById(id);
            if (!selectedProduct.isPresent())
                throw new DataNotFoundException(PRODUCT_NOT_FOUND);

            if (selectedProduct.get().getMerchant().getId() != merchantExistingOpt.get().getId())
                throw new DataNotFoundException("Mechant dont have this product");

            Product deletedProduct = productService.deleteProduct(selectedProduct.get());

            ProductRespDto respDto = modelMapper.map(deletedProduct, ProductRespDto.class);
            data.put("users", respDto);
            response.put("data", data);
            response.put("status", "success");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataNotFoundException e) {
            response.put("status", "fail");
            data.put("products", e.getMessage());
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
