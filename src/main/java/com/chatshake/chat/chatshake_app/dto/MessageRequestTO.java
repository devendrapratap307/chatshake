package com.chatshake.chat.chatshake_app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageRequestTO {
    private String content;
    private String sender;
    private String roomId;
    private LocalDateTime messageTime;
    private MessageType type;
}
