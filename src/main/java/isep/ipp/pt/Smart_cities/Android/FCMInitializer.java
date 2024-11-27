package isep.ipp.pt.Smart_cities.Android;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FCMInitializer {
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;
    Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

    @PostConstruct
    public void initialize() {
        try {
            ClassPathResource resource = new ClassPathResource(firebaseConfigPath);
            if (!resource.exists()) {
                logger.error("Firebase configuration file not found: " + firebaseConfigPath);
                return;
            }
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application initialized");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
