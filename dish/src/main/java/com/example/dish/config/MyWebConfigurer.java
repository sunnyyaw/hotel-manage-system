package com.example.dish.config;

import com.example.dish.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Configuration
public class MyWebConfigurer implements WebMvcConfigurer {
    @Autowired
    private StorageProperties storageProperties;
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePath = this.getClass().getResource("./").toString()+"images/";
        String path = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8);
        registry.addResourceHandler("/files/**")
                .addResourceLocations(path);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8080","http://localhost:8081")
                .allowedMethods(CorsConfiguration.ALL)
                .allowedHeaders(CorsConfiguration.ALL);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/verifyCode","/sms","/validate");
    }
}
