package com.github.tuding.blindbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class BlindboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlindboxApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("PRC"));
		log.info("Spring boot application running in PRC timezone (GMT+8) :" + LocalDateTime.now());
	}

	@PreDestroy
	public void onExit() {
		log.info("Shutdown hook be triggered, server would be stop soon ...");
	}

}
