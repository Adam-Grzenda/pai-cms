package pl.put.cmsbackend.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class PostgresDataSourceProvider {

    private final PostgresConfig postgresConfig;


    @Bean
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresConfig.getDriver());
        dataSource.setUsername(postgresConfig.getUsername());
        dataSource.setPassword(postgresConfig.getPassword());
        dataSource.setUrl(postgresConfig.getUrl());
        return dataSource;
    }

}
