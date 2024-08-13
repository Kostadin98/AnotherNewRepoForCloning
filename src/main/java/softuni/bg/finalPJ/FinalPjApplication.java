package softuni.bg.finalPJ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinalPjApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalPjApplication.class, args);
	}

}
