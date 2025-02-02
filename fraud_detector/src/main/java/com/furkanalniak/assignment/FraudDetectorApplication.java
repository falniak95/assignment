package com.furkanalniak.assignment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableKafka
@EnableMongoRepositories
public class FraudDetectorApplication {
	private static final Logger logger = LogManager.getLogger(FraudDetectorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectorApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(KafkaListenerEndpointRegistry registry) {
		return args -> {
			logger.info("Starting Kafka listeners...");
			registry.getListenerContainers().forEach(container -> {
				logger.info("Container ID: {}, isRunning: {}", 
					container.getListenerId(), 
					container.isRunning());
				if (!container.isRunning()) {
					logger.info("Starting container: {}", container.getListenerId());
					container.start();
				}
			});
		};
	}
}
