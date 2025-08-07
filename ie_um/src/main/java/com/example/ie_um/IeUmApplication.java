package com.example.ie_um;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IeUmApplication {

    public static void main(String[] args) {
        SpringApplication.run(IeUmApplication.class, args);
    }

}
