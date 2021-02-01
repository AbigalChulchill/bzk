package net.bzk.flow;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = { "net.bzk" })
@SpringBootApplication
public class BzkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BzkflowApplication.class, args);
	}
	
	 @Bean(name = "threadPoolTaskExecutor")
	 public ExecutorService  threadPoolTaskExecutor() {
	      ThreadPoolExecutor threadpool = new ThreadPoolExecutor(
	                10,
	                20,
	                50,
	                TimeUnit.DAYS,
	                new ArrayBlockingQueue<Runnable>(100),
	                new ThreadPoolExecutor.CallerRunsPolicy());
	        return threadpool;
	  }

	 @Primary
	 @Bean(name ="BzkModelJsonMapper")
	 public ObjectMapper BzkModelJsonMapper() {
		 return BzkFlowUtils.getFlowJsonMapper();
	 }
	 
	 
	 
}
