package com.chatshake.chat.chatshake_app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

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
}
