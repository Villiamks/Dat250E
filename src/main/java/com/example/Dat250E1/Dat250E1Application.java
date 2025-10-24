package com.example.Dat250E1;

import com.example.Dat250E1.Models.Poll;
import com.example.Dat250E1.Services.LoginService;
import com.example.Dat250E1.Services.PollService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.valkey.Jedis;
import io.valkey.JedisPool;

@SpringBootApplication
public class Dat250E1Application {

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(Dat250E1Application.class, args);
        context.getBean(LoginService.class);
        context.getBean(PollService.class);
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
