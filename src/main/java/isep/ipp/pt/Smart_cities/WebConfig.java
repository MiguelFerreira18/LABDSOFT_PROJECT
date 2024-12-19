package isep.ipp.pt.Smart_cities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/event_images/**")
                .addResourceLocations("file:///C:/Users/bizua/Desktop/ISEP_1SEMESTRE/LABDSOFT/LABDSOFT_PROJECT/event_images/");
    }
}