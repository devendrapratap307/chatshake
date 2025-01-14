package com.chatshake.chat.chatshake_app.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final  JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        HttpSession session = null;
        if (request instanceof ServletServerHttpRequest) {
            session = ((ServletServerHttpRequest) request).getServletRequest().getSession(true);
            if (session != null) {
                String sessionId = session.getId();
                attributes.put("HTTP_SESSION_ID", sessionId);
                System.out.println("HTTP Session ID: " + session.getId());
//                session.setAttribute("userId", "sampleUserId"); // Test value
            } else {
                System.out.println("HTTP session is null!");
            }
        }

        URI uri = request.getURI();
        String query = uri.getQuery();

        Object authHeader = request.getHeaders().get("Authorization");
        Object userId2 = request.getHeaders().get("userId");

        if (authHeader != null) {
            attributes.put("Authorization", authHeader);
        }
        if (userId2 != null) {
            attributes.put("userId", userId2);
        }
        System.out.println("Session ID in HandshakeInterceptor: " + attributes.get("simpSessionId"));
        if (query != null) {
            Map<String, String> queryParams = Arrays.stream(query.split("&"))
                    .map(param -> param.split("="))
                    .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

            String token = queryParams.get("token");
            if (token != null) {
                try {
                    String userId = jwtUtil.validateTokenAndGetUserId(token);
                    attributes.put("userId", userId);
                    session.setAttribute("userId", userId);
                    System.out.println("attributes===================="+attributes);
                    return true;
                } catch (Exception e) {
                    log.error("Invalid JWT token: {}", e.getMessage());
                }
            }
        }

//        // Extract the token from the headers
//        log.info("Headers: {}", request.getHeaders());
//        System.out.println("=====================>   "+request.getHeaders());
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        log.info("Authorization: {}", authHeader);
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                String userId = jwtUtil.validateTokenAndGetUserId(token);
//                if (userId != null) {
//                    attributes.put("userId", userId);
//                    log.info("beforeHandshake : {}", userId);
//                    return true;
//                }
//            } catch (Exception e) {
//                // Token validation failed
//                System.out.println("Invalid JWT token: " + e.getMessage());
//            }
//        }
        // If no valid token is provided, reject the handshake
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Post-handshake actions (optional)
    }
}
