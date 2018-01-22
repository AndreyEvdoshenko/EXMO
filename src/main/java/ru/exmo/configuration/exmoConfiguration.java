package ru.exmo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.api.tradingApi.exmoTradingApi;

import javax.sql.DataSource;

/**
 * Created by Andrash on 05.01.2018.
 */
@Configuration
@PropertySource(value = "classpath:key.properties")
@PropertySource(value = "classpath:db.properties")
@ComponentScan(basePackages = {"ru.exmo"})
public class exmoConfiguration {

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("driverClassName"));
        dataSource.setUrl(environment.getProperty("url"));
        dataSource.setUsername(environment.getProperty("user"));
        dataSource.setPassword(environment.getProperty("password"));
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcOperations() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public tradingApi tradingApi(){
        tradingApi apiClient = new exmoTradingApi();
        apiClient.setKey(environment.getProperty("key"));
        apiClient.setSecret(environment.getProperty("secret"));
        return apiClient;
    }

}
