package isep.ipp.pt.Smart_cities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetAddress;

@SpringBootApplication
public class SmartCitiesApplication {
	private static final Logger logger = LoggerFactory.getLogger(SmartCitiesApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SmartCitiesApplication.class, args);

		try {

			String localIp = InetAddress.getLocalHost().getHostAddress();
			int port = context.getEnvironment().getProperty("server.port", Integer.class, 8080);

			logger.info("Application running at http://{}:{}", localIp, port);
		} catch (Exception e) {
			logger.error("Error getting local IP address: " + e.getMessage());
		}
	}

}
