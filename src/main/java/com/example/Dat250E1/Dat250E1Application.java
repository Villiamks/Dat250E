package com.example.Dat250E1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Dat250E1Application {

    public static void main(String[] args) {
        SpringApplication.run(Dat250E1Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry regestry) {
                regestry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("POST","PUT","GET","DELETE");
            }
        };
    }

}
