package com.chatshake.chat.chatshake_app.Dao;

import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.models.UserTempBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public SearchRespTO searchUser(SearchReqTO searchReq, boolean pageFlag) {
        SearchRespTO searchResReturn = new SearchRespTO();
        Query query = new Query();
        if (searchReq.getLabel() != null && !searchReq.getLabel().isEmpty()) {
            query.addCriteria(Criteria.where("username").regex(".*" + searchReq.getLabel() + ".*", "i"));
        }
        Pageable pageable = PageRequest.of(searchReq.getPage(), searchReq.getLimit(), Sort.by(Sort.Direction.ASC, "username"));
        query.with(pageable);
        List<UserTempBO> users = mongoTemplate.find(query, UserTempBO.class);
        if(pageFlag){
            Query countQuery = Query.of(query).limit(-1).skip(-1); // Reset skip/limit for accurate count
            long total = mongoTemplate.count(countQuery, UserTempBO.class);
            searchResReturn.setTotalRow(total);
        }
        searchResReturn.setDataList(users);
        return searchResReturn;
    }


}
