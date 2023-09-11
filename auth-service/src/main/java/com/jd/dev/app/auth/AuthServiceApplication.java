package com.jd.dev.app.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@PropertySource("classpath:bootstrap.properties")
public class AuthServiceApplication implements CommandLineRunner {
    private final BCryptPasswordEncoder passwordEncode;

    public AuthServiceApplication(BCryptPasswordEncoder passwordEncode) {
        this.passwordEncode = passwordEncode;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String password = "12345";

        for (int i = 0; i < 4; i++) {
            String passwordBCrypt = passwordEncode.encode(password);
            System.out.println(passwordBCrypt);
        }
    }
}
