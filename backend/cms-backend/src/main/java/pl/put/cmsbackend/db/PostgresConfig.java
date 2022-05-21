package pl.put.cmsbackend.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "postgres", ignoreUnknownFields = false)
@RequiredArgsConstructor
@Getter
public class PostgresConfig {

    private final String url;
    private final String username;
    private final String password;
    private final String driver;

}
