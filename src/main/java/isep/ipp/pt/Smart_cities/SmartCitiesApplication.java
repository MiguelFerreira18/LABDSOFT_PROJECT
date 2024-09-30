package isep.ipp.pt.Smart_cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SmartCitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCitiesApplication.class, args);
	}

}
