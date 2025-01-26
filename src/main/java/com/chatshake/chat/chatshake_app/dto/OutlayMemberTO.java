package com.chatshake.chat.chatshake_app.dto;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class OutlayMemberTO {
    private String id;
    private String memberId;
    private ENUM.SPLIT_TYPE splitType;
    private double percentage;
    private double amount;
}
