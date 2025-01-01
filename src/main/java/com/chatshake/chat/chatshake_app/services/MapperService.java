package com.chatshake.chat.chatshake_app.services;

import java.util.Collection;
import java.util.List;

public interface MapperService {
    <E, U> E map(final U data, Class<E> className);

    <E, U> List<E> map(final Collection<U> dataList, Class<E> className);
}
