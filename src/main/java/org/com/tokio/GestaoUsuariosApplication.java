package org.com.tokio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GestaoUsuariosApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GestaoUsuariosApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GestaoUsuariosApplication.class, args);
    }
} 