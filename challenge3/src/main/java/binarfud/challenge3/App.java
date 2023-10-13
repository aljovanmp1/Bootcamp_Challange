package binarfud.challenge3;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import binarfud.challenge3.service.AppService;

@SpringBootApplication
public class App {
    public static void main(String[] args) {    
        // AppService app = new AppService();
        // app.start();

        ConfigurableApplicationContext context =  SpringApplication.run(App.class, args);
        AppService appService = context.getBean(AppService.class);
        
        appService.initiateData();
        appService.start();
    }
}
