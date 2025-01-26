package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.models.OutlayMemberBO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageRequestTO {
    private String id;
    private String content;
    private String sender;
    private String roomId;
    private ENUM.MESSAGE_TYPE type;
    private ENUM.MESSAGE_STATUS status;
    private LocalDateTime timeStamp;

    // outlay data
    private double totalAmount;
    private String paidBy;
    private List<OutlayMemberTO> memberList;

    private String senderName;
}
