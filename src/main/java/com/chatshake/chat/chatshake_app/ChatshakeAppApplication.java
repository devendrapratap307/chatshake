package com.chatshake.chat.chatshake_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableMongoAuditing
//@EnableRedisHttpSession
public class ChatshakeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatshakeAppApplication.class, args);
	}

}
