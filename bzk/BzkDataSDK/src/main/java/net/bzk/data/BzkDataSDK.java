package net.bzk.data;

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
public class BzkDataSDK {

	public static void main(String[] args) {
		SpringApplication.run(BzkDataSDK.class, args);
	}

}
