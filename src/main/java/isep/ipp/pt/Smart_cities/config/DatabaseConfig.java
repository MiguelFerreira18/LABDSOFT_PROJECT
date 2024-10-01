package isep.ipp.pt.Smart_cities.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String pass;

    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    public DataSource dataSource() {
        // NOTE: in case there is a need to debug it
        // System.out.println("username");
        // System.out.println(username);
        // System.out.println("password");
        // System.out.println(pass);
        // System.out.println("URL");
        // System.out.println(url);

        return DataSourceBuilder.create()
                .url(url).username(username).password(pass).build();
    }
}
