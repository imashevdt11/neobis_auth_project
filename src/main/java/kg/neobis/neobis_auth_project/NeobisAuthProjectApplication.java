package kg.neobis.neobis_auth_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class NeobisAuthProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeobisAuthProjectApplication.class, args);
	}

}
