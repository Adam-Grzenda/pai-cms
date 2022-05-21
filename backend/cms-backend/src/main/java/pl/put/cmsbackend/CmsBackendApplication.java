package pl.put.cmsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsBackendApplication.class, args);
    }

}
