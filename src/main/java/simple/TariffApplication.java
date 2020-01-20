package simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TariffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TariffApplication.class, args);
	}

}
