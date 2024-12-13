package admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.backend.core", "admin"
})
@EnableJpaRepositories(basePackages = "com.backend.core")
@EntityScan(basePackages = "com.backend.core")
public class SpringAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAdminServiceApplication.class, args);
    }

}
