package com.chatshake.chat.chatshake_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ChatshakeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatshakeAppApplication.class, args);
	}

}
