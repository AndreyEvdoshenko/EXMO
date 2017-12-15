package ru.exmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Андрей on 15.12.2017.
 */
@ComponentScan
@EnableAutoConfiguration
public class application {
    public static void main(String[] args) {
        SpringApplication.run(application.class, args);
    }
}
