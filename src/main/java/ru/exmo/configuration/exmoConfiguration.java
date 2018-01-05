package ru.exmo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.api.tradingApi.tradingApiClient;

/**
 * Created by Andrash on 05.01.2018.
 */
@Configuration
@PropertySource(value = "classpath:key.properties")
@ComponentScan(basePackages = {"ru.exmo"})
public class exmoConfiguration {

    @Autowired
    Environment environment;

    @Bean
    public tradingApi tradingApi(){
        tradingApi apiClient = new tradingApiClient();
        apiClient.setKey(environment.getProperty("key"));
        apiClient.setSecret(environment.getProperty("secret"));
        return apiClient;
    }

}
