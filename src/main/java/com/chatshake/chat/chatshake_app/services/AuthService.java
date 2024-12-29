package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.jwt.JwtUtil;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public HashMap<String, String> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getId() !=null &&  user.getPassword() !=null && passwordEncoder.matches(password, user.getPassword())) {
            HashMap<String, String> jwtTokenMap = new HashMap<>();
            jwtTokenMap.put("token", jwtUtil.generateToken(username, user.getId()));
            return jwtTokenMap;
        }
        throw new RuntimeException("Invalid username or password");
    }
}

