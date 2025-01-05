package com.chatshake.chat.chatshake_app.Dao;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.RoomRequestTO;
import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.models.MessageBO;
import com.chatshake.chat.chatshake_app.models.RoomRequestBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ChatRoomDaoImpl implements ChatRoomDao{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<RoomRequestBO> findRoomRequests(RoomRequestTO roomRequest, List<ENUM.REQUEST_TYPE> excludedStatuses) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                new Criteria().andOperator(
                        Criteria.where("reqFrom").is(roomRequest.getReqFrom()),
                        Criteria.where("reqTo").is(roomRequest.getReqTo())
                ),
                new Criteria().andOperator(
                        Criteria.where("reqFrom").is(roomRequest.getReqTo()),
                        Criteria.where("reqTo").is(roomRequest.getReqFrom())
                )
        ));
        if(excludedStatuses != null && !excludedStatuses.isEmpty()){
            query.addCriteria(Criteria.where("status").nin(excludedStatuses));
        }

        return mongoTemplate.find(query, RoomRequestBO.class);
    }

    @Override
    public SearchRespTO searchRequests(SearchReqTO searchReq, boolean receiveFlag, boolean pageFlag) {
        SearchRespTO searchResReturn = new SearchRespTO();
        if(searchReq !=null && searchReq.getParticipant() !=null){
            Query query = new Query();
            if(receiveFlag){
                query.addCriteria(Criteria.where("reqTo").is(searchReq.getParticipant()));
            } else {
                query.addCriteria(Criteria.where("reqFrom").is(searchReq.getParticipant()));
            }
            query.addCriteria(Criteria.where("status").nin(Arrays.asList(ENUM.REQUEST_TYPE.ACCEPTED, ENUM.REQUEST_TYPE.DENIED, ENUM.REQUEST_TYPE.REJECTED)));
            Pageable pageable = PageRequest.of(searchReq.getPage(), searchReq.getLimit(), Sort.by(Sort.Direction.ASC, "id"));
            query.with(pageable);
            List<RoomRequestBO> requests =  mongoTemplate.find(query, RoomRequestBO.class);
            if(pageFlag){
                Query countQuery = Query.of(query).limit(-1).skip(-1); // Reset skip/limit for accurate count
                long total = mongoTemplate.count(countQuery, RoomRequestBO.class);
                searchResReturn.setTotalRow(total);
                searchResReturn.setPageCount(searchReq.getPage());
            }
            searchResReturn.setDataList(requests);
        }
        return searchResReturn;
    }

    @Override
    public List<ChatRoomBO> findRooms(String participant, ENUM.ROOM_TYPE type, ENUM.ROOM_STATUS status) {
        if(participant !=null){
            Query query = new Query();
            query.addCriteria(Criteria.where("participants.id").is(participant));
            if(type !=null){
                query.addCriteria(Criteria.where("type").is(type));
            }
            if(status !=null){
                query.addCriteria(Criteria.where("status").is(status));
            } else {
                query.addCriteria(Criteria.where("status").nin(ENUM.ROOM_STATUS.DEL));
            }
            return mongoTemplate.find(query, ChatRoomBO.class);
        }
        return null;
    }

    @Override
    public SearchRespTO searchRooms(SearchReqTO searchReq, boolean pageFlag) {
        SearchRespTO searchResReturn = new SearchRespTO();
        if(searchReq !=null){
            Query query = new Query();
            if (searchReq.getLabel() != null && !searchReq.getLabel().isEmpty()) {
                query.addCriteria(Criteria.where("roomName").regex(".*" + searchReq.getLabel() + ".*", "i"));
            }
            if(searchReq.getParticipant() !=null){
                query.addCriteria(Criteria.where("participants.id").is(searchReq.getParticipant()));
            }
            if(searchReq.getType() !=null && !searchReq.getType().toString().trim().isEmpty()){
                query.addCriteria(Criteria.where("type").is(searchReq.getType()));
            }
            if(searchReq.getStatus() !=null){
                query.addCriteria(Criteria.where("status").is(searchReq.getStatus()));
            } else {
                query.addCriteria(Criteria.where("status").nin(ENUM.ROOM_STATUS.DEL));
            }
            Pageable pageable = PageRequest.of(searchReq.getPage(), searchReq.getLimit(), Sort.by(Sort.Direction.ASC, "updatedDate"));
            query.with(pageable);
            List<ChatRoomBO> rooms =  mongoTemplate.find(query, ChatRoomBO.class);
            if(pageFlag){
                Query countQuery = Query.of(query).limit(-1).skip(-1); // Reset skip/limit for accurate count
                long total = mongoTemplate.count(countQuery, ChatRoomBO.class);
                searchResReturn.setTotalRow(total);
                searchResReturn.setPageCount(searchReq.getPage());
            }
            searchResReturn.setDataList(rooms);
        }
        return searchResReturn;
    }

    @Override
    public SearchRespTO searchMessages(SearchReqTO searchReq, boolean pageFlag) {
        SearchRespTO searchResReturn = new SearchRespTO();
        if(searchReq !=null && searchReq.getRoomId() !=null){
            Query query = new Query();
            query.addCriteria(Criteria.where("roomId").is(searchReq.getRoomId()));
//            query.addCriteria(Criteria.where("content").ne(null).andOperator(
//                    Criteria.where("content").ne("")).andOperator(
//                    Criteria.where("content").exists(true)));
            query.addCriteria(new Criteria().andOperator(
                    Criteria.where("content").ne(null),
                    Criteria.where("content").ne(""),
                    Criteria.where("content").exists(true)
            ));

            Pageable pageable = PageRequest.of(searchReq.getPage(), searchReq.getLimit(), Sort.by(Sort.Direction.ASC, "updatedDate"));
            query.with(pageable);
            List<MessageBO> messages =  mongoTemplate.find(query, MessageBO.class);
            if(pageFlag){
                Query countQuery = Query.of(query).limit(-1).skip(-1); // Reset skip/limit for accurate count
                long total = mongoTemplate.count(countQuery, MessageBO.class);
                searchResReturn.setTotalRow(total);
                searchResReturn.setPageCount(searchReq.getPage());
            }
            searchResReturn.setDataList(messages);
        }
        return searchResReturn;
    }

    @Override
    public MessageBO saveOrUpdateMessage(MessageBO message) {
        return mongoTemplate.save(message);
    }

}
