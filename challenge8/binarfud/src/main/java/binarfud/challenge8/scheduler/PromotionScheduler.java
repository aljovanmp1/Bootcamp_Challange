package binarfud.challenge8.scheduler;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import binarfud.challenge8.model.Product;
import binarfud.challenge8.service.FCMService;
import binarfud.challenge8.service.ProductService;

@Component
public class PromotionScheduler {

    final private FCMService fcmService;
    final private ProductService productService;

    Logger logger = LoggerFactory.getLogger(PromotionScheduler.class);


    public PromotionScheduler(FCMService fcmService, ProductService productService) {
        this.fcmService = fcmService;
        this.productService = productService;
    }

    @Scheduled(cron = "00 00 12 * * *")
    public void cronJob(){
        Optional<Product> product = productService.getRandProduct();
        String msg = "Ayuh cek produk ini: " + product.get().getProductName();
        try{
            fcmService.sendMessageToTopic("Jam makan siang nih", msg, "promo");
        }
        catch(ExecutionException | InterruptedException e){
            logger.error(e.getMessage(), e);
        }
    }
    
}
