package com.example.matricula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class MatriculaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MatriculaApplication.class, args);
    }
}
