package net.bzk.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.OPTIONS, RequestMethod.HEAD }, allowedHeaders = "*", origins = "*")
@Validated
@Controller
@Slf4j
public class TailController {

	@Inject
	private SimpMessageSendingOperations messageSender;

	@Value("${bzk.trail.path:/tmp/log/spring.log}")
	private String trailFilePath;

	private boolean keepReading;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

	@MessageMapping("/tail")
	public void tail() throws InterruptedException, IOException {
		if (keepReading) {
			return;
		}
		keepReading = true;
		try (InputStream is = new FileInputStream(new File(trailFilePath))) {
			Reader targetReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(targetReader);
			String line;
			while (keepReading) {
				line = br.readLine();
				if (line == null) {
					// wait until there is more of the file for us to read
					Thread.sleep(1000);
				} else {
					System.out.println(line);
					sendLine(line);
				}
			}
		}
	}

	private void sendLine(String line) {
		String path = "/topic/tail";
		messageSender.convertAndSend(path, line);
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "tail/reading", method = RequestMethod.GET)
	public ReadingInfo getReadingInfo() {
		return new ReadingInfo(keepReading);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "tail/stop", method = RequestMethod.POST)
	public void stop() {
		keepReading = false;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "tail/clear", method = RequestMethod.POST)
	public void clearLogFile() throws FileNotFoundException {
		keepReading = false;
		try( PrintWriter pw = new PrintWriter(trailFilePath)){
			pw.print("");
		}
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class ReadingInfo {
		private boolean keepReading;
	}

}
