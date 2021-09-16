package net.bzk.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"net.bzk"})
@SpringBootApplication
public class BzkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(BzkflowApplication.class, args);
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ExecutorService threadPoolTaskExecutor() {
        ThreadPoolExecutor threadpool = new ThreadPoolExecutor(
                64,
                128,
                3,
                TimeUnit.DAYS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return threadpool;
    }

    @Primary
    @Bean(name = "BzkModelJsonMapper")
    public ObjectMapper BzkModelJsonMapper() {
        return BzkFlowUtils.getFlowJsonMapper();
    }

    @Bean
    public MappingMongoConverter mongoConverter(MongoDatabaseFactory mongoFactory, MongoMappingContext mongoMappingContext) throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mongoConverter.setMapKeyDotReplacement("-DOT");
        return mongoConverter;
    }

}
