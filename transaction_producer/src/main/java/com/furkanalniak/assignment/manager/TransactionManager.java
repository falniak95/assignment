package com.furkanalniak.assignment.manager;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.service.TransactionKafkaProducer;
import com.furkanalniak.assignment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionManager {

  private final TransactionService transactionService;
  private final TransactionKafkaProducer transactionKafkaProducer;

  @Autowired
  public TransactionManager(
      TransactionService transactionService, TransactionKafkaProducer transactionKafkaProducer) {
    this.transactionService = transactionService;
    this.transactionKafkaProducer = transactionKafkaProducer;
  }

  public Mono<Transaction> generateRandomTransaction() {
    return transactionService
        .generateRandomTransaction()
        .doOnNext(
            transaction -> {
              transactionKafkaProducer.sendTransaction(transaction);
            });
  }

  public Mono<Transaction> generateFraudulentTransaction() {
    return transactionService
        .generateFraudulentTransaction()
        .doOnNext(
            transaction -> {
              transactionKafkaProducer.sendTransaction(transaction);
            });
  }
}
