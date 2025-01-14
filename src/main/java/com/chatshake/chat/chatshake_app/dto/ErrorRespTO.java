package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.MSG_CONST;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorRespTO {
    private int status;
    private String code;
    private String message;
    private Integer totalErrorCount;
    private List<ErrorFieldTO> fieldErrors = new ArrayList<>();

    public ErrorRespTO() {
    }
    public ErrorRespTO(int status, String code, String msg) {
        super();
        this.status = status;
        this.code = code;
        this.message = msg;
    }
    public static ErrorRespTO buildError(Errors errors) {
        ErrorRespTO error = new ErrorRespTO();
        Integer count=0;
        error.setStatus(400);
        error.setCode("M005");
        error.setMessage(MSG_CONST.MSG.get("M005"));
        for (org.springframework.validation.FieldError feildError : errors.getFieldErrors()) {
            count++;

            ErrorFieldTO fieldError = new ErrorFieldTO();
            fieldError.setFieldName(feildError.getField());
            fieldError.setMessage(feildError.getDefaultMessage());
            fieldError.setCode(feildError.getCode());
            error.getFieldErrors().add(fieldError);
        }
        error.setTotalErrorCount(count);
        return error;
    }
}
