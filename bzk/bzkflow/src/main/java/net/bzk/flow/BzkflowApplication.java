package net.bzk.flow;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.bzk.flow.enums.Enums;
import net.bzk.flow.model.Domain;
import net.bzk.flow.model.RunLog;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.run.dao.DomainRepository;
import net.bzk.flow.run.dao.RunLogDao;
import net.bzk.infrastructure.RandomConstant;
import org.springframework.boot.CommandLineRunner;
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
    CommandLineRunner init(DomainRepository domainRepository, RunLogDao ldao) {

        return args -> {

            Domain n = new Domain();
            n.setId(System.currentTimeMillis());
            n.setDomain("test.com");
            VarMap v = new VarMap();
            v.put("dd",123);
            n.setFlowVar(v);
            domainRepository.save(n);

            Domain obj2 = domainRepository.findFirstByDomain("test.com");
            System.out.println(obj2);


            RunLog rl = new RunLog();
            rl.setUid(RandomConstant.randomUid(16));
            rl.setState(Enums.RunState .ActionCall);
            VarMap bmap = new VarMap();
            bmap.put("zzz",5);
            rl.setBoxVar(bmap);
            ldao.save(rl);


        };

    }

}
