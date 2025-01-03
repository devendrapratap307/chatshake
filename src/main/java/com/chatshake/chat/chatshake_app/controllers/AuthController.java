package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.dto.UserTO;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import com.chatshake.chat.chatshake_app.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator userValidator;

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
//            ValidationErrors validationError = ;// need to create responseTO
            return new ResponseEntity<>(result.getFieldError(), HttpStatus.OK);
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User userReturn = userRepository.save(this.modelMapper.map(user, User.class));
        return new ResponseEntity<>(userReturn,HttpStatus.CREATED);
    }
}

