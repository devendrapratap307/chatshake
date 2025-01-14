package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisStreamService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisStreamService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    public void addMessageToStream(String streamName, MessageRequestTO message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            RecordId recordId = redisTemplate
                    .opsForStream()
                    .add(StreamRecords.newRecord().in(streamName).ofObject(jsonMessage));
            log.info("Message added to Redis stream '{}' with Record ID: {}", streamName, recordId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize MessageRequestTO: {}", message, e);
            throw new RuntimeException("Failed to serialize MessageRequestTO", e);
        } catch (Exception e) {
            // Handle Redis or other unexpected errors
            log.error("Failed to add message to Redis stream '{}'", streamName, e);
            throw new RuntimeException("Failed to add message to Redis stream", e);
        }
    }
}
