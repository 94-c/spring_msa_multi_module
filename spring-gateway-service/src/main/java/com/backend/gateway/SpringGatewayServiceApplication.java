package com.backend.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.backend.gateway")
public class SpringGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayServiceApplication.class, args);
    }

}
