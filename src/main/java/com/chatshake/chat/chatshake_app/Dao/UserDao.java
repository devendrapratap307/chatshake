package com.chatshake.chat.chatshake_app.Dao;

import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;

public interface UserDao {
    SearchRespTO searchUser(SearchReqTO searchReq, boolean pageFlag);
}
