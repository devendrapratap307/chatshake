package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestTO {
    private String id;
    private String reqFrom;
    private String reqTo;
    private ENUM.REQUEST_TYPE status;

    private String label;
    private String username;
}
