package com.backend.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.backend.gateway"}
)
public class SpringGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayServiceApplication.class, args);
    }

}
