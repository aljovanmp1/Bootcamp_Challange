package challenge_4.binarfud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import challenge_4.binarfud.controller.HomeController;

@SpringBootApplication
public class BinarfudApplication {

	public static void main(String[] args) {
		HomeController home = SpringApplication.run(BinarfudApplication.class, args)
			.getBean(HomeController.class);
		
			home.home();
	}

}
