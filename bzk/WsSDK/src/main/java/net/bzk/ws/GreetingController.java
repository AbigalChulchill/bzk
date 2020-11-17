package net.bzk.ws;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

//@Controller
//public class GreetingController {
//
//	@Inject
//	private SimpMessageSendingOperations messageSender;
//
//	@MessageMapping("/hello")
//	@SendTo("/topic/greetings")
//	public Greeting greeting(HelloMessage message) throws Exception {
//		Thread.sleep(1000); // simulated delay
//		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//	}
//
//	@MessageMapping("/test")
//	public void testSendMultiple() throws InterruptedException {
//		String path = "/topic/test";
//		Map m = new HashMap<>();
//		m.put("aaa", "bbb");
//		for (int i = 0; i < 100; i++) {
//			messageSender.convertAndSend(path, m);
//			Thread.currentThread().sleep(100);
//		}
//	}
//
//}
