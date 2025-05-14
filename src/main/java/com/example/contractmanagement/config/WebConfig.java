package com.example.contractmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径
                .allowedOrigins("http://localhost:5173") // 允许来自前端 localhost:5173 的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许这些方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true); // 允许携带 Cookie
    }
}
