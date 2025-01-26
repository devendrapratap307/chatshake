package com.chatshake.chat.chatshake_app.models;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "outlay_message")
public class OutlayMessageBO {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String content;

    private double totalAmount;
    private String paidBy;
    private List<OutlayMemberBO> memberList;

    private ENUM.MESSAGE_TYPE type; //OUTLAY
    private ENUM.MESSAGE_STATUS status;
    private LocalDateTime timeStamp;
}
