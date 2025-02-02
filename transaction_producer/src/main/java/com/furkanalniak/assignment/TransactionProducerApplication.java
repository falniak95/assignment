package com.furkanalniak.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TransactionProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactionProducerApplication.class, args);
  }
}
