package com.example.springsecurityapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigImage implements WebMvcConfigurer {  // Конфигурация  для загрузки фотографий
    @Value("${upload.path}")
    private String uploadPath; // внедряем данные поля путь где будут хранится фотографии


    // Переопределяем метод
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**") // если обращаемся к шаблону...
        .addResourceLocations("file:///" +uploadPath +"/");  // ... надо идти искать по... // , для Win /// для iOC b LInux //

    }
}
