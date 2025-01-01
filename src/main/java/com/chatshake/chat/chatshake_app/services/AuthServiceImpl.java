package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.Dao.UserDao;
import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;
import com.chatshake.chat.chatshake_app.dto.UserTO;
import com.chatshake.chat.chatshake_app.jwt.JwtUtil;
import com.chatshake.chat.chatshake_app.models.User;
import com.chatshake.chat.chatshake_app.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ChatRoomService roomService;

    @Override
    public HashMap<String, String> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getId() !=null &&  user.getPassword() !=null && passwordEncoder.matches(password, user.getPassword())) {
            HashMap<String, String> jwtTokenMap = new HashMap<>();
            jwtTokenMap.put("token", jwtUtil.generateToken(username, user.getId(), user.getName()!=null ? user.getName() : null));
            return jwtTokenMap;
        }
        throw new RuntimeException("Invalid username or password");
    }

    @Override
    public SearchRespTO searchUser(SearchReqTO searchReq, boolean pageFlag) {
        return this.userDao.searchUser(searchReq, pageFlag);
    }

    @Override
    public UserTO saveUser(UserTO user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User userReturn = userRepository.save(this.modelMapper.map(user, User.class));
        if(userReturn!=null && userReturn.getId() !=null){
            roomService.createChatRoom(userReturn.getId(), ENUM.ROOM_TYPE.SELF, Arrays.asList(userReturn.getId()));
        }
        return this.modelMapper.map(userReturn, UserTO.class);
    }
}
