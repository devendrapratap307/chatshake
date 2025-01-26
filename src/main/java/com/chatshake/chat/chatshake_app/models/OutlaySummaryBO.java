package com.chatshake.chat.chatshake_app.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "outlay_summary")
public class OutlaySummaryBO {
    @Id
    private String id;
    private String roomId;
    private String memberId;
    private double inFlow;  // need to collect
    private double outFlow; // need to pay

}
