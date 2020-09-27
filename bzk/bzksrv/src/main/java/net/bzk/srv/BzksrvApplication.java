package net.bzk.srv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = { "net.bzk" })
@SpringBootApplication
public class BzksrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(BzksrvApplication.class, args);
	}

}
