package net.bzk.gists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = { "net.bzk" })
@SpringBootApplication
public class FlowGistsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowGistsApplication.class, args);
	}
	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	 
	 
}
