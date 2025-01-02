package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.Dao.ChatRoomDao;
import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.*;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.models.MessageBO;
import com.chatshake.chat.chatshake_app.models.RoomRequestBO;
import com.chatshake.chat.chatshake_app.models.UserTempBO;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.repositories.RoomRequestRepository;
import com.chatshake.chat.chatshake_app.repositories.UserTempRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService{
    @Autowired
    private ChatRoomDao chatRoomDao;

    @Autowired
    private RoomRequestRepository roomRequestRepository;

    @Autowired
    private UserTempRepository userTempRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MapperService mapperService;
    @Override
    public String roomRequest(RoomRequestTO roomRequest) {
        if(roomRequest != null && roomRequest.getReqTo() != null && roomRequest.getReqFrom() !=null){
            List<RoomRequestBO> roomRequests = this.chatRoomDao.findRoomRequests(roomRequest, Arrays.asList(ENUM.REQUEST_TYPE.REJECTED, ENUM.REQUEST_TYPE.DENIED));
            if(roomRequests != null && !roomRequests.isEmpty()){
                RoomRequestBO currRoomReq = roomRequests.get(0);
                if (currRoomReq != null && currRoomReq.getReqFrom() !=null && currRoomReq.getReqTo() !=null && currRoomReq.getStatus() != null){
                    if(currRoomReq.getStatus().equals(ENUM.REQUEST_TYPE.ACCEPTED)){
                        return "M010";
                    } else if(roomRequest.getReqFrom().equals(currRoomReq.getReqFrom()) && roomRequest.getReqTo().equals(currRoomReq.getReqTo()) && currRoomReq.getStatus().equals(ENUM.REQUEST_TYPE.PENDING)){
                        return "M007";
                    } else if(roomRequest.getReqFrom().equals(currRoomReq.getReqTo()) && roomRequest.getReqTo().equals(currRoomReq.getReqFrom()) && currRoomReq.getStatus().equals(ENUM.REQUEST_TYPE.PENDING)){
                        currRoomReq.setStatus(ENUM.REQUEST_TYPE.ACCEPTED);
                        RoomRequestBO roomReqReturn = roomRequestRepository.save(currRoomReq);
                        if(roomReqReturn != null){
                            this.createChatRoom(null, ENUM.ROOM_TYPE.CHAT, Arrays.asList(roomRequest.getReqTo(), roomRequest.getReqFrom()));
                        }
                        return "M009";
                    }
                }
            } else {
                RoomRequestBO roomReqReturn = roomRequestRepository.save(modelMapper.map(roomRequest, RoomRequestBO.class));
                if(roomReqReturn != null){
                    return "M008";
                }
            }
        }
        return null;
    }

    @Override
    public String roomRequestAccetance(RoomRequestTO roomRequest, boolean acceptFlag){
        if(roomRequest != null && roomRequest.getReqTo() != null && roomRequest.getReqFrom() !=null){
            List<RoomRequestBO> roomRequests = this.chatRoomDao.findRoomRequests(roomRequest, Arrays.asList(ENUM.REQUEST_TYPE.REJECTED, ENUM.REQUEST_TYPE.DENIED));
            if(roomRequests != null && !roomRequests.isEmpty()) {
                RoomRequestBO currRoomReq = roomRequests.get(0);
                if (currRoomReq != null && currRoomReq.getReqFrom() != null && currRoomReq.getReqTo() != null && currRoomReq.getStatus() != null) {
                    if(currRoomReq.getStatus().equals(ENUM.REQUEST_TYPE.ACCEPTED)){
                        return "M010";
                    }
                    if(acceptFlag){
                        currRoomReq.setStatus(ENUM.REQUEST_TYPE.ACCEPTED);
                        RoomRequestBO roomReqReturn = roomRequestRepository.save(currRoomReq);
                        this.createChatRoom(null, ENUM.ROOM_TYPE.CHAT, Arrays.asList(roomRequest.getReqTo(), roomRequest.getReqFrom()));
                        return "M009";
                    } else if(roomRequest.getId() !=null){
                        this.roomRequestRepository.deleteById(roomRequest.getId());
                        return "M011";
                    }
                }
            }
        }
        return null;
    }

    @Override
    public SearchRespTO searchRequests(SearchReqTO searchReq, boolean receiveFlag, boolean pageFlag){
        if(searchReq!=null && searchReq.getParticipant() !=null){
            SearchRespTO searchData = this.chatRoomDao.searchRequests(searchReq, receiveFlag, pageFlag);
            if(searchData != null && searchData.getDataList() !=null && !searchData.getDataList().isEmpty()){
                List<RoomRequestTO> requestList = this.mapperService.map(searchData.getDataList(), RoomRequestTO.class);
                if(requestList!=null && !requestList.isEmpty()){
                    for (RoomRequestTO requestRow : requestList) {
                        if(requestRow != null && requestRow.getReqFrom() !=null && requestRow.getReqTo() !=null){
                            if(receiveFlag){
                                Optional<UserTempBO> member = this.userTempRepository.findById(requestRow.getReqFrom());
                                if(member !=null){
                                    if(member.get().getName() !=null) requestRow.setLabel(member.get().getName());
                                    if(member.get().getUsername() !=null) requestRow.setUsername(member.get().getUsername());
                                }
                            } else {
                                Optional<UserTempBO> member = this.userTempRepository.findById(requestRow.getReqTo());
                                if(member !=null){
                                    if(member.get().getName() !=null) requestRow.setLabel(member.get().getName());
                                    if(member.get().getUsername() !=null) requestRow.setUsername(member.get().getUsername());
                                }
                            }
                        }
                    }
                    searchData.setDataList(requestList);
                }
            }
            return searchData;
        }
        return null;
    }

    @Override
    public ChatRoomTO createChatRoom(String roomName, ENUM.ROOM_TYPE type, List<String> participants){
        if(participants !=null && !participants.isEmpty()){
            if(type !=null && type.equals(ENUM.ROOM_TYPE.SELF)){
                List<ChatRoomBO> selfRoom = findRooms(participants.get(0), ENUM.ROOM_TYPE.SELF, ENUM.ROOM_STATUS.ACT);
                if(selfRoom !=null && !selfRoom.isEmpty()){
                    return null;
                }
            }
            ChatRoomBO chatRoom = new ChatRoomBO();
            chatRoom.setRoomName(roomName);
            chatRoom.setType(type);
            chatRoom.setStatus(ENUM.ROOM_STATUS.ACT);
            chatRoom.setParticipants(participants);
            return modelMapper.map(chatRoomRepository.save(chatRoom), ChatRoomTO.class);
        }
        return null;
    }

    @Override
    public List<ChatRoomBO> findRooms(String participant, ENUM.ROOM_TYPE type, ENUM.ROOM_STATUS status) {
        if(participant != null){
            return this.chatRoomDao.findRooms(participant, type, status);
        }
        return null;
    }

    @Override
    public SearchRespTO searchRooms(SearchReqTO searchReq, boolean pageFlag) {
        if(searchReq != null){
            SearchRespTO searchResp = this.chatRoomDao.searchRooms(searchReq, pageFlag);
            if(searchResp !=null){
                if( searchResp.getDataList() !=null){
                    List<ChatRoomTO> chatList = this.mapperService.map(searchResp.getDataList(), ChatRoomTO.class) ;
                    if(chatList != null){
                        for (ChatRoomTO chatRoomRow : chatList) {
                            if(searchReq!=null && searchReq.getParticipant() !=null && chatRoomRow != null && chatRoomRow.getParticipants() != null && chatRoomRow.getType() !=null && chatRoomRow.getType().equals(ENUM.ROOM_TYPE.CHAT)){
                                for (String participant : chatRoomRow.getParticipants()) {
                                    if(participant!=null && !participant.equals(searchReq.getParticipant())){
                                        Optional<UserTempBO> member = this.userTempRepository.findById(participant);
                                        if(member !=null){
                                            if(member.get().getName() !=null){
                                                chatRoomRow.setRoomName(member.get().getName());
                                            } else if(member.get().getUsername() !=null){
                                                chatRoomRow.setRoomName(member.get().getUsername());
                                            } else {
                                                chatRoomRow.setRoomName("Unknown");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        searchResp.setDataList(chatList);
                    }
                }
                return searchResp;
            }
        }
        return null;
    }

    @Override
    public SearchRespTO searchMessages(SearchReqTO searchReq, boolean pageFlag) {
        if(searchReq != null){
            SearchRespTO searchResp = this.chatRoomDao.searchMessages(searchReq, pageFlag);
            if(searchResp !=null){
                if( searchResp.getDataList() !=null){
                    List<MessageRequestTO> msgList = this.mapperService.map(searchResp.getDataList(), MessageRequestTO.class) ;
                    if(msgList != null && !msgList.isEmpty()){
                        searchResp.setDataList(msgList);
                    }
                }
                return searchResp;
            }
        }
        return null;
    }

    @Override
    public MessageRequestTO saveOrUpdateMessage(MessageRequestTO msg) {
        if(msg != null){
//            MessageBO message = new MessageBO();
//            message.setRoomId(msg.getRoomId());
//            message.setSender(msg.getSender());
//            message.setContent(msg.getContent());
//            message.setType(msg.getType());
//            message.setStatus(msg.getStatus());
//            message.setTimeStamp(LocalDateTime.now());
            msg.setTimeStamp(LocalDateTime.now());
            MessageBO messageReturn = this.chatRoomDao.saveOrUpdateMessage(this.mapperService.map(msg, MessageBO.class));
            if(messageReturn!=null){
                return this.mapperService.map(messageReturn, MessageRequestTO.class);
            }
        }
        return null;
    }
}
