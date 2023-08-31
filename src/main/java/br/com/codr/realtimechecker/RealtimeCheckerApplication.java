package br.com.codr.realtimechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories
@EnableKafka
public class RealtimeCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtimeCheckerApplication.class, args);
	}

}
