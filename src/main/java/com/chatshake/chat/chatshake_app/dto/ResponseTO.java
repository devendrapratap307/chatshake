package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.MSG_CONST;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseTO {
    private int status;
    private String message;
    private String code;
    private String path;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, String> errorList;

    public static ResponseTO build(int status, String code, String path, String key, Object data) {
        ResponseTO response = new ResponseTO();
        response.setStatus(status);
        response.setPath(path);
        response.setMessage(MSG_CONST.MSG.get(code));
        response.setCode(code);
        response.putData(key, data);
        return response;
    }

    public void putData(String key, Object Value) {
        data.put(key, Value);
    }
}
