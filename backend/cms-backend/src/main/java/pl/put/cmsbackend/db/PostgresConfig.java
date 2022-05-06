package pl.put.cmsbackend.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class PostgresConfig {
    @Value("${postgres.url}")
    private String jdbcUrl;

    @Value("${postgres.username}")
    private String username;

    @Value("${postgres.password}")
    private String password;

    @Value("${postgres.driver}")
    private String postgresDriver;


    @Bean
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresDriver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(jdbcUrl);
        return dataSource;
    }
}
