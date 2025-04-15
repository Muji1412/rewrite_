package com.example.rewrite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class RewriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewriteApplication.class, args);
    }

}
