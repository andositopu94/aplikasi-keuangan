package brajaka.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinanceBrajakaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceBrajakaApplication.class, args);
	}

}
