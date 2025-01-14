package com.chatshake.chat.chatshake_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchRespTO <T> {
    private List<T> dataList;
    private long totalRow;
    private int pageCount;
}
