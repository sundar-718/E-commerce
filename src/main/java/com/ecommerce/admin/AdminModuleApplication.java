package com.ecommerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AdminModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminModuleApplication.class, args);
    }
}
