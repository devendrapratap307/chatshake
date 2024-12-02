package com.chatshake.chat.chatshake_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageRequest {
    private String content;
    private String sender;
    private String roomId;
    private LocalDateTime messageTime;
}
