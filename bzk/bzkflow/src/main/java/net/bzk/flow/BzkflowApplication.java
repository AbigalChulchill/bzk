package net.bzk.flow;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.bzk.flow.model.parse.OTypeDeserializer;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.convert.OType;


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
		 ObjectMapper ans = new ObjectMapper();
		 SimpleModule module = new SimpleModule();
		 
		 Reflections reflections = new Reflections("net.bzk");    
		 Set<Class<? extends OType>> classes = reflections.getSubTypesOf(OType.class);
		 List<Class<? extends OType>> incs = classes.stream().filter(c-> CommUtils.hasChild(c, classes) ).collect(Collectors.toList());
		 CommUtils.pl(JsonUtils.toJson(incs));
		 for(Class< ? extends OType> c : incs) {
			 module.addDeserializer(c, new OTypeDeserializer(ans));
		 }
		 ans.registerModule(module);
		 return ans;
	 }
	 
	 
	 
}
