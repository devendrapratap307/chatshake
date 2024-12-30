package com.chatshake.chat.chatshake_app.validators;

import com.chatshake.chat.chatshake_app.controllers.AuthController;
import com.chatshake.chat.chatshake_app.dto.UserTO;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = AuthController.class)
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UserTO) {
            UserTO user = (UserTO) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "COM01E", "required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "COM01E", "required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "COM01E", "required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "COM01E", "required");
            if(user.getUsername()!=null){
                User user1 = userRepository.findByUsername(user.getUsername());
                if(user1!=null){
                    errors.rejectValue("username", "COM02E", "Already present.");
                }
            }
        }
    }
}
