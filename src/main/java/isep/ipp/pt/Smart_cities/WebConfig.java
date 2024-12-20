package isep.ipp.pt.Smart_cities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String relativePath = "event_images/";
        String absolutePath = Paths.get(relativePath).toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/event_images/**")
                .addResourceLocations(absolutePath);
    }
}