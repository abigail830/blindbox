package com.github.tuding.blindbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
@Slf4j
public class BlindboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlindboxApplication.class, args);
	}

	@PreDestroy
	public void onExit() {
		log.info("Shutdown hook be triggered, server would be stop soon ...");
	}

}
