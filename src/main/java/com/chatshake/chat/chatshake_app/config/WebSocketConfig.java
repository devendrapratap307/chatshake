package com.chatshake.chat.chatshake_app.config;

import com.chatshake.chat.chatshake_app.jwt.JwtHandshakeInterceptor;
import com.chatshake.chat.chatshake_app.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        // app/chat
        // server-side: @MessagingMapping("/chat")
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil),  new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
//                .setWebSocketEnabled(true);
        // connection will establish at  /chat   endpoint
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendBufferSizeLimit(512 * 1024);
        registry.setMessageSizeLimit(16 * 1024);
    }
}
