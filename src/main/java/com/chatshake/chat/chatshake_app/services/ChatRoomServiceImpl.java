package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.Dao.ChatRoomDao;
import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.*;
import com.chatshake.chat.chatshake_app.models.*;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.repositories.RoomRequestRepository;
import com.chatshake.chat.chatshake_app.repositories.UserTempRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
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
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public ChatRoomServiceImpl(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public String roomRequest(RoomRequestTO roomRequest) {
        if(roomRequest != null && roomRequest.getReqTo() != null && roomRequest.getReqFrom() !=null){
            if(roomRequest.getReqTo().equals(roomRequest.getReqFrom())){
                return null;
            }
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
            if(roomRequest.getReqTo().equals(roomRequest.getReqFrom())){
                return null;
            }
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
                                if(member.isPresent()){
                                    if(member.get().getName() !=null) requestRow.setLabel(member.get().getName());
                                    if(member.get().getUsername() !=null) requestRow.setUsername(member.get().getUsername());
                                }
                            } else {
                                Optional<UserTempBO> member = this.userTempRepository.findById(requestRow.getReqTo());
                                if(member.isPresent()){
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
            List<ParticipantBO> pts = new ArrayList<>();
            participants.forEach(pcId->{
                ParticipantBO participant = new ParticipantBO();
                participant.setId(pcId);
                pts.add(participant);
            });
            if(!pts.isEmpty()){
                chatRoom.setParticipants(pts);
                return modelMapper.map(chatRoomRepository.save(chatRoom), ChatRoomTO.class);
            }
        }
        return null;
    }

    @Override
    public ChatRoomTO createChatRoom(ChatRoomTO chatRoom) {
        if(chatRoom !=null){
            ChatRoomBO chatRoomReturn = chatRoomRepository.save(mapperService.map(chatRoom, ChatRoomBO.class));
            return modelMapper.map(chatRoomReturn, ChatRoomTO.class);
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
                            if(searchReq!=null && searchReq.getParticipant() !=null && chatRoomRow != null && chatRoomRow.getParticipants() != null && !chatRoomRow.getParticipants().isEmpty()  && chatRoomRow.getType() !=null){
                                if(chatRoomRow.getType().equals(ENUM.ROOM_TYPE.CHAT)){
                                    for (ParticipantTO participant : chatRoomRow.getParticipants()) {
                                        if(participant!=null && participant.getId() !=null && !participant.getId().equals(searchReq.getParticipant())){
                                            Optional<UserTempBO> member = this.userTempRepository.findById(participant.getId());
                                            if(member.isPresent()){
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
                                } else if(chatRoomRow.getType().equals(ENUM.ROOM_TYPE.SELF)){
                                    Optional<UserTempBO> member = this.userTempRepository.findById(searchReq.getParticipant());
                                    if(member.isPresent()){
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
            HashMap<String, String> memberNameMap = new HashMap<>();
            SearchRespTO searchResp = this.chatRoomDao.searchMessages(searchReq, pageFlag);
            if(searchResp !=null){
                if( searchResp.getDataList() !=null){
                    List<MessageRequestTO> msgList = this.mapperService.map(searchResp.getDataList(), MessageRequestTO.class) ;
                    if(msgList != null && !msgList.isEmpty()){
                        msgList.forEach(msg->{
                            if(msg.getSender() != null){
                                if(memberNameMap.containsKey(msg.getSender())){
                                    msg.setSenderName(memberNameMap.get(msg.getSender()));
                                } else {
                                    Optional<UserTempBO> member = this.userTempRepository.findById(msg.getSender());
                                    if(member.isPresent()){
                                        if(member.get().getName() !=null){
                                            msg.setSenderName(member.get().getName());
                                            memberNameMap.put(msg.getSender(), member.get().getName());
                                        } else if(member.get().getUsername() !=null){
                                            msg.setSenderName(member.get().getUsername());
                                            memberNameMap.put(msg.getSender(), member.get().getUsername());
                                        } else {
                                            msg.setSenderName("Unknown");
                                            memberNameMap.put(msg.getSender(), "Unknown");
                                        }
                                    } else {
                                        msg.setSenderName("Unknown");
                                        memberNameMap.put(msg.getSender(), "Unknown");
                                    }
                                }
                            }
                        });
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

    @Override
    public ChatRoomTO chatRoomById(String roomId) {
        Optional<ChatRoomBO> chatRoom = this.chatRoomRepository.findById(roomId);
        if(chatRoom.isPresent()){
            ChatRoomTO chatRoomTO = mapperService.map(chatRoom, ChatRoomTO.class);
            if(chatRoomTO !=null && chatRoomTO.getParticipants() !=null && !chatRoomTO.getParticipants().isEmpty()){
                chatRoomTO.getParticipants().forEach(pts->{
                    if(pts.getId() !=null){
                        Optional<UserTempBO> member = this.userTempRepository.findById(pts.getId());
                        if(member.isPresent()){
                            if(member.get().getName() !=null){
                                pts.setLabel(member.get().getName());
                            } else if(member.get().getUsername() !=null){
                                pts.setLabel(member.get().getUsername());
                            } else {
                                pts.setLabel("Unknown");
                            }
                        }
                    }
                });
            }
            return chatRoomTO;
        }
        return null;
    }

    @Override
    public void userConnection(String userId) {
        if (userId != null) {
            redisTemplate.opsForValue().set("user-online:" + userId, "true");
            OnlineStatusTO onlineStatus =  new OnlineStatusTO(userId, true);
            messagingTemplate.convertAndSend("/topic/online-status/" + userId, onlineStatus);
            sendPendingNotifications(userId);
        } else {
            log.error("User ID is null in userConnection");
        }
    }

    private void sendPendingNotifications(String userId) {
        List<String> pendingMessages = redisTemplate.opsForList().range("offline-queue:" + userId, 0, -1);
        if (pendingMessages != null && !pendingMessages.isEmpty()) {
            pendingMessages.forEach(message -> {
                // Send notification to user's WebSocket channel
                messagingTemplate.convertAndSend("/topic/private/" + userId, message);
            });

            // Clear the offline queue for the user
            redisTemplate.delete("offline-queue:" + userId);
        }
    }
}
