package com.chatshake.chat.chatshake_app.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.messaging.simp.SimpSessionScope;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Optional;

@Configuration
public class Configs {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {// for AuditBO
        return () -> Optional.of(UserContext.getCurrentUser()!=null && UserContext.getCurrentUser().getId() !=null ? UserContext.getCurrentUser().getId() : "unknown"); // Replace with actual user retrieval logic
    }

    @Bean
    public SimpSessionScope simpSessionScope() {
        return new SimpSessionScope();
    }

    @Bean
    public HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }

}
