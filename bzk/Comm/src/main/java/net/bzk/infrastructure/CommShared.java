package net.bzk.infrastructure;

import org.dozer.DozerBeanMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.domi" })
@SpringBootApplication
public class CommShared {



	public static void main(String[] args) {
		SpringApplication.run(CommShared.class, args);
	}
	
	

	@Bean("prettyJosnMapper")
	public ObjectMapper prettyJosnMapper() {
		return  new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Bean
	public DozerBeanMapper dozerBeanMapper() {
		return new DozerBeanMapper();
	}
	
	

}
