package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomTO extends AuditTO {
    private String id;
    private String roomName;
    private ENUM.ROOM_TYPE type;
    private ENUM.ROOM_STATUS status;
    private List<String> participants;
    private List<String> admins;
}
