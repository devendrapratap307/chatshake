package com.chatshake.chat.chatshake_app.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MapperServiceImpl implements MapperService {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public <E, U> E map(U data, Class<E> className) {
        if(data !=null && className !=null){
            modelMapper.map(data, className);
        }
        return null;
    }

    @Override
    public <E, U> List<E> map(Collection<U> dataList, Class<E> className) {
        List<E> returnData = null;
        if (dataList != null && !dataList.isEmpty() && className !=null) {
            returnData = new ArrayList<>();
            for (U entityListObject : dataList) {
                returnData.add(map(entityListObject, className));
            }
        }
        return returnData;
    }
}
