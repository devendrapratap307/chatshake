package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;
import com.chatshake.chat.chatshake_app.dto.UserTO;
import com.chatshake.chat.chatshake_app.jwt.JwtUtil;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


public interface AuthService {
    HashMap<String, String> authenticate(String username, String password);
    SearchRespTO searchUser(SearchReqTO searchReq, boolean pageFlag);
    UserTO saveUser(UserTO user);

}

