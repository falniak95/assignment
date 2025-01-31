package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@EnableKafka
@Slf4j
@RequiredArgsConstructor
public class TransactionKafkaConsumer {
    private final TransactionRepository transactionRepository;

    @KafkaListener(topics = "transaction-events", groupId = "fraud-detector-group")
    public void consumeTransaction(Transaction transaction) {
        log.info("Received transaction: {}", transaction.getTransactionId());
        
        Mono.just(transaction)
            .map(this::performFraudCheck)
            .flatMap(transactionRepository::save)
            .subscribe(
                savedTransaction -> log.info("Transaction processed and saved: {}", savedTransaction.getTransactionId()),
                error -> log.error("Error processing transaction: {}", error.getMessage())
            );
    }

    private Transaction performFraudCheck(Transaction transaction) {
        // Implement fraud detection logic here
        boolean isFraudulent = detectFraud(transaction);
        if (isFraudulent) {
            transaction.setFraudulent(true);
            transaction.setFraudReason("Suspicious activity detected");
        }
        return transaction;
    }

    private boolean detectFraud(Transaction transaction) {
        // Example fraud detection logic
        return transaction.getRandomString().length() > 50; // Basit bir örnek
    }
} 