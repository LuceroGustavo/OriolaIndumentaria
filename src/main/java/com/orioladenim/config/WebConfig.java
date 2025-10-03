package com.orioladenim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar el directorio de uploads para servir archivos estáticos
        String absolutePath = Paths.get(uploadPath).toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath + "/");
        
        // Asegurar que los recursos estáticos (CSS, JS) se sirvan correctamente
        registry.addResourceHandler("/css/**", "/js/**", "/img/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", "classpath:/static/img/")
                .setCachePeriod(3600);
    }
}

