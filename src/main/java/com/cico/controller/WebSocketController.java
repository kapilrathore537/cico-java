package com.cico.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
public class WebSocketController {
	@MessageMapping("/socket")
	@SendTo("/queue/Chatmessages")
	public String send(String message) {
		System.out.println("------------------------"+message);
		return message;
	}
}
