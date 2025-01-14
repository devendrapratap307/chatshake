package com.chatshake.chat.chatshake_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineStatusTO {
    private String userId;
    private boolean onlineFlag;
    public OnlineStatusTO(String userId, boolean status){
        this.userId = userId;
        this.onlineFlag = status;
    }
}
