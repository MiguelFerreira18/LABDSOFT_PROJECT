package isep.ipp.pt.Smart_cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SmartCitiesApplication {
	@Value("${spring.datasource.username}")
	private static String username;
	@Value("${LABDSOFT_DB_USER}")
	private String dbUser;

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SmartCitiesApplication.class, args);

	}

	@PostConstruct
	public void printEnv() {
		System.out.println("LABDSOFT_DB_USER: " + env.getProperty("LABDSOFT_DB_USER"));
		System.out.println("LABDSOFT_DB_PASS: " + env.getProperty("LABDSOFT_DB_PASS"));
		System.out.println("LABDSOFT_DB_HOST: " + env.getProperty("LABDSOFT_DB_HOST"));
		System.out.println("LABDSOFT_DB_DEV_DB_NAME: " + env.getProperty("LABDSOFT_DB_DEV_DB_NAME"));
	}

	@PostConstruct
	public void printDbUser() {
		System.out.println("DB User from @Value: " + dbUser);
	}

}
