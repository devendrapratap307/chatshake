package com.chatshake.chat.chatshake_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorFieldTO {
    private String fieldName;
    private String message;
    private String code;
}
