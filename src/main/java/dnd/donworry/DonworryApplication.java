package dnd.donworry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DonworryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonworryApplication.class, args);
	}

}
