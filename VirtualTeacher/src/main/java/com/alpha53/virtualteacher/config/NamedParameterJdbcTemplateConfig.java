package com.alpha53.virtualteacher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class NamedParameterJdbcTemplateConfig {

    private final String dbUrl, dbUsername, dbPassword, driverClassName;

    @Autowired
    public NamedParameterJdbcTemplateConfig(Environment env) {
        dbUrl = env.getProperty("database.url");
        dbUsername = env.getProperty("database.username");
        dbPassword = env.getProperty("database.password");
        driverClassName = env.getProperty("database.driver");
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
    @Bean
    public NamedParameterJdbcTemplate NamedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }

}

