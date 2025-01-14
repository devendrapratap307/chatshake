package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.dto.*;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import com.chatshake.chat.chatshake_app.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator userValidator;

    private ResponseTO resp;

    @PostMapping("/login")
    public HashMap<String, String> login(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password);
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody UserTO user, Errors result, HttpServletRequest request) {
        if(userValidator.supports(UserTO.class)){
            userValidator.validate(user,result);
        }
        if (result.hasErrors()) {
            ErrorRespTO errorResp = ErrorRespTO.buildError(result);
            return new ResponseEntity<>(errorResp, HttpStatus.OK);
        }
        resp = ResponseTO.build(200, "M001","/user/add", "user", this.authService.saveUser(user));
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }

    @PostMapping("/user/search")
    public ResponseEntity<?> searchUser(@RequestBody SearchReqTO searchReqTO, @RequestParam(value = "pageFlag", required = false) final boolean pageFlag, Errors result, HttpServletRequest request) {
        SearchRespTO searchResp = this.authService.searchUser(searchReqTO, pageFlag);
        resp = ResponseTO.build(200, "M001","/user/search", "user", searchResp);
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }
}

