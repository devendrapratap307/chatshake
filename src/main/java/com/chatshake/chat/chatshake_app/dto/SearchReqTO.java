package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchReqTO {
    int page;
    int limit;

    String label;
    String name;
    String username;

    String roomId;
    String participant; //userId
    ENUM.ROOM_TYPE type;
    ENUM.ROOM_STATUS status;
}
