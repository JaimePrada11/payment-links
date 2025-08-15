package com.laura.paymentlinks.paymentlinks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://127.0.0.1:5501/", "http://127.0.0.1:5502/", "*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);

//                // Permitir el acceso a los endpoints de Swagger
//                registry.addMapping("/doc/swagger-ui.html").allowedOrigins("http://localhost:5173", "http://localhost:8080");
//                registry.addMapping("/v3/api-docs/**").allowedOrigins("http://localhost:5173", "http://localhost:8080");
            }
        };
    }
}