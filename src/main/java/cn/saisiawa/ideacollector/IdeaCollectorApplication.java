package cn.saisiawa.ideacollector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author saisiawa
 */
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
@Slf4j
public class IdeaCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdeaCollectorApplication.class, args);
        log.info("=====Started Success!!!=======");
    }

}
