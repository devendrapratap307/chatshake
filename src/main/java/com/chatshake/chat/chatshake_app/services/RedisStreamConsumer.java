package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class RedisStreamConsumer {

    private static final String STREAM_NAME = "chat-stream";
    private static final String CONSUMER_GROUP = "group";
    private static final int READ_COUNT = 10;
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(2);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomService roomService;
    private final ObjectMapper objectMapper;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RedisStreamConsumer(RedisTemplate<String, Object> redisTemplate, ChatRoomService roomService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void startAsyncListening() {
        executorService.submit(this::listenToStream);
    }
    @PreDestroy
    public void stopListening() {
        executorService.shutdownNow();
    }


    private void listenToStream() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<MapRecord<String, Object, Object>> messages = redisTemplate
                        .opsForStream()
                        .read(StreamReadOptions.empty().count(READ_COUNT).block(BLOCK_TIMEOUT),
                                StreamOffset.latest(STREAM_NAME));
                if (messages != null && !messages.isEmpty()) {
                    messages.forEach(this::processMessage);
                }
            } catch (Exception e) {
                log.error("Error while reading messages from Redis stream: {}", STREAM_NAME, e);
            }
        }
    }

    private void processMessage(MapRecord<String, Object, Object> record) {
        try {
            String jsonMessage = record.getValue().toString();
            log.info("Received JSON message: {}", jsonMessage);
            jsonMessage = fixMalformedJson(jsonMessage);
            // Parse the JSON
            Map<String, Object> messageMap = objectMapper.readValue(jsonMessage, Map.class);
            Map<String, Object> payload = (Map<String, Object>) messageMap.get("payload");
            MessageRequestTO chatMessage = objectMapper.convertValue(payload, MessageRequestTO.class);
            log.info("Processed chat message: {}", chatMessage);
            // Process or save the chat message
            roomService.saveOrUpdateMessage(chatMessage);
        } catch (Exception e) {
            log.error("Error processing message: {}", record.getId(), e);
        }
    }


    private String fixMalformedJson(String jsonMessage) {
        if (jsonMessage.contains("payload={")) {
            jsonMessage = jsonMessage.replace("payload={", "\"payload\":{").replace("}", "}");
            jsonMessage = jsonMessage.replace("=", ":");
        }
        return jsonMessage;
    }

}
